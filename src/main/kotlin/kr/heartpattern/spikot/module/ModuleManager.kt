package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf

/**
 * Manage all modules. [ModuleHandler]
 */
object ModuleManager {
    private lateinit var moduleInterceptor: Set<Pair<IModuleInterceptor, ModuleInterceptor>>
    private val shutdown = LinkedList<ModuleHandler>()

    /**
     * Initialize module manager
     */
    internal fun load() {
        moduleInterceptor = SpikotPluginManager
            .annotationIterator<ModuleInterceptor>()
            .asSequence()
            .map { (type) ->
                type.createInstance() as IModuleInterceptor to
                    type.findAnnotation<ModuleInterceptor>()!!
            }
            .toSet()
    }

    /**
     * Shutdown module manager
     */
    internal fun shutdown() {
        for (handler in shutdown)
            if (handler.state == IModule.State.ENABLE)
                handler.disable()
    }

    /**
     * Find interceptor for given module
     * @param type Module type
     * @return Set of interceptor for given module
     */
    @Suppress("UNCHECKED_CAST")
    internal fun findInterceptor(type: KClass<*>): Set<IModuleInterceptor> {
        return moduleInterceptor
            .asSequence()
            .filter { it.second.targetClass.isSuperclassOf(type) }
            .map { it.first }
            .toSet()
    }

    /**
     * Create new [ModuleHandler]
     */
    fun createModule(module: KClass<*>, owner: SpikotPlugin): ModuleHandler {
        return ModuleHandler(module, owner)
    }

    /**
     * Create new [ModuleHandler]
     */
    fun createModule(module: IModule, owner: SpikotPlugin): ModuleHandler {
        return ModuleHandler(module::class, owner, module)
    }

    /**
     * Set module handler to disable on shutdown
     */
    fun disableOnShutdown(handler: ModuleHandler) {
        shutdown.add(handler)
    }
}