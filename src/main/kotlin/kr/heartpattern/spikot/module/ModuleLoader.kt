package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.logger
import kr.heartpattern.spikot.misc.AbstractMutableProperty
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.plugin.FindAnnotation
import kr.heartpattern.spikot.utils.nonnull
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Annotate module processor.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@FindAnnotation
annotation class ModuleInterceptor(
    val targetClass: KClass<*> = Any::class
)

/**
 * Handle module's whole lifecycle
 */
class ModuleHandler(val type: KClass<*>, val owner: SpikotPlugin, created: IModule? = null) {
    internal object MutableStateProperty : AbstractMutableProperty<IModule.State>(IModule.StateProperty)

    private val interceptors = ModuleManager.findInterceptor(type)
    /**
     * Handled module
     */
    var module: IModule? = null
        private set

    /**
     * Property map that store module's data used in module interceptor
     */
    val context: MutablePropertyMap = MutablePropertyMap()

    /**
     * Module's state
     */
    var state: IModule.State by context.mutableDelegate(MutableStateProperty).nonnull()
        private set

    init {
        onDebug {
            logger.info("Create module: ${type.simpleName}")
        }

        state = try {
            module = created ?: type.createInstance() as IModule
            interceptors.forEach { interceptor ->
                interceptor.onCreate(this)
            }
            IModule.State.CREATE
        } catch (e: Throwable) {
            logger.warn("Error occur", e)
            interceptors.forEach { interceptor ->
                interceptor.onError(this, IModule.State.CREATE, e)
            }
            IModule.State.ERROR
        }
    }

    /**
     * Load module
     * @return true if load successfully
     */
    fun load(): Boolean {
        return performStep(IModule.State.CREATE, IModule.State.LOAD, { this.onLoad(context) }, IModuleInterceptor::onLoad)
    }

    /**
     * Enable module
     * @return true if enable successfully
     */
    fun enable(): Boolean {
        return performStep(IModule.State.LOAD, IModule.State.ENABLE, IModule::onEnable, IModuleInterceptor::onEnable)
    }

    /**
     * Disable module
     * @return true if disable successfully
     */
    fun disable(): Boolean {
        val result = performStep(IModule.State.ENABLE, IModule.State.DISABLE, IModule::onDisable, IModuleInterceptor::onDisable)
        module = null // Perform GC
        return result
    }

    /**
     * Perform each step of module's lifecycle
     * @param previous Expected previous state
     * @param next State after perform current step
     * @param task Module call task
     * @param intercept Interceptor call task
     * @return Whether step is success
     */
    private inline fun performStep(previous: IModule.State, next: IModule.State, task: IModule.() -> Unit, intercept: IModuleInterceptor.(ModuleHandler) -> Unit): Boolean {
        check(state == previous) { "Module is already $next" }
        onDebug {
            logger.info("${state.readable} module: ${type.simpleName}")
        }
        state = try {
            module!!.task()
            interceptors.forEach { interceptor ->
                interceptor.intercept(this)
            }
            next
        } catch (e: Throwable) {
            logger.warn("Error occur while ${state.readable} module: ${type.simpleName}")
            interceptors.forEach { interceptor ->
                interceptor.onError(this, next, e)
            }
            IModule.State.ERROR
        }
        return state != IModule.State.ERROR
    }
}