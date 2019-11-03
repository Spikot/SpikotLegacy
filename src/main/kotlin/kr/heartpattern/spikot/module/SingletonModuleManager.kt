@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.logger
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@PublishedApi
@kr.heartpattern.spikot.Bootstrap(loadOrder = -10000)
internal object SingletonModuleManager : kr.heartpattern.spikot.IBootstrap {
    @PublishedApi
    internal val instances: MutableMap<KClass<*>, ModuleHandler> = HashMap()

    override fun onStartup() {
        for ((module, plugin) in SpikotPluginManager.annotationIterator<Module>()) {
            onDebug {
                logger.info("Find module: ${module.simpleName}")
            }
            if (!module.canLoad())
                continue

            instances[module] = ModuleManager.createModule(module, plugin)
        }

        val remover = LinkedList<KClass<*>>()

        for (handler in instances.values
            .asSequence()
            .sortedBy { it.type.findAnnotation<Module>()!!.loadOrder })
            if (!handler.load())
                remover.add(handler.type)

        for (remove in remover)
            instances.remove(remove)
        remover.clear()

        for (handler in instances.values
            .asSequence()
            .sortedBy { it.type.findAnnotation<Module>()!!.loadOrder })
            if (!handler.enable())
                remover.add(handler.type)

        for (remove in remover)
            instances.remove(remove)
    }

    override fun onShutdown() {
        instances.values.toList().reversed().forEach { handler ->
            handler.disable()
        }
    }
}