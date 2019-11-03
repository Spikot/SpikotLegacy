package kr.heartpattern.spikot

import kr.heartpattern.spikot.plugin.FindAnnotation
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

/**
 * Annotate bootstrap class
 * @param loadOrder Load order of class. Lower run faster
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@FindAnnotation(impl = [IBootstrap::class])
annotation class Bootstrap(val loadOrder: Int)

/**
 * Server bootstrapping class which call on load state
 */
interface IBootstrap {
    /**
     * Call when server load
     */
    fun onLoad() {}

    /**
     * Call when server start
     */
    fun onStartup() {}

    /**
     * Call when server shutdown
     */
    fun onShutdown() {}
}

/**
 * Call bootstrap classes
 */
internal object BootstrapManager : IBootstrap {
    private lateinit var instances: List<IBootstrap>
    override fun onLoad() {
        instances = SpikotPluginManager
            .annotationIterator<Bootstrap>()
            .asSequence()
            .map { (type) -> logger.catchAll("Cannot create bootstrap instance") { type.createInstance() as IBootstrap } }
            .filterNotNull()
            .sortedBy { it::class.findAnnotation<Bootstrap>()!!.loadOrder }
            .toList()

        for (instance in instances) {
            logger.catchAll("Cannot load bootstrap") {
                instance.onLoad()
            }
        }
    }

    override fun onStartup() {
        for (instance in instances) {
            logger.catchAll("Cannot start bootstrap") {
                instance.onStartup()
            }
        }
    }

    override fun onShutdown() {
        for (bootstrap in instances.reversed()) {
            logger.catchAll("Cannot shutdown bootstrap") {
                bootstrap.onShutdown()
            }
        }
    }
}