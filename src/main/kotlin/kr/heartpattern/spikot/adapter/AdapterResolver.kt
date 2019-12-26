package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.ModuleHandler
import kr.heartpattern.spikot.module.ModuleManager
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Adapter resolver that select which adapter to be used by default
 */
abstract class AdapterResolver<T : IAdapter>(val target: KClass<T>) : AbstractModule() {
    private lateinit var handler: ModuleHandler

    /**
     * Default adapter instance
     */
    @Suppress("UNCHECKED_CAST")
    val default: T
        get() = handler.module as T

    @Suppress("UNCHECKED_CAST")
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        handler = SpikotPluginManager
            .annotationIterator<Adapter>()
            .asSequence()
            .filter { (type) -> type.isSubclassOf(target) }
            .filter { (_, _, annotation) ->
                annotation.target == Nothing::class || annotation.target.isSubclassOf(target)
            }
            .map { (type, plugin) -> ModuleManager.createModule(type, plugin) }
            .filter { canApply(it.module!! as T) }
            .maxWith(Comparator { o1, o2 -> if (select(o1.module!! as T, o2.module!! as T) === o1.module!!) 1 else -1 })
            ?: throw IllegalStateException("Cannot find proper adapter for ${target.simpleName}")

        handler.load()
        handler.enable()
    }

    override fun onDisable() {
        handler.disable()
    }

    /**
     * Check whether given adapter is applicable
     */
    abstract fun canApply(adapter: T): Boolean

    /**
     * Select more fitted adapter
     * @param a First adapter candidate which can be apply
     * @param b Second adapter candidate which can be apply
     * @return More fit adapter for current environment
     */
    abstract fun select(a: T, b: T): T
}