@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.Bootstrap
import kr.heartpattern.spikot.IBootstrap
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.logger
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@PublishedApi
@Bootstrap
internal object SingletonModuleManager : IBootstrap {
    @PublishedApi
    internal val typeHandlerMap: MutableMap<KClass<*>, ModuleHandler> = HashMap()
    private val instances = LinkedList<ModuleHandler>()

    override fun onStartup() {
        val modules = LinkedList<KClass<*>>()
        val modulePlugin = HashMap<KClass<*>, SpikotPlugin>()

        for ((module, plugin) in SpikotPluginManager.annotationIterator<Module>()) {
            onDebug {
                logger.info("Find module: ${module.simpleName}")
            }
            modulePlugin[module] = plugin
            modules += module
        }

        val sorted = sortModuleDependencies(modules)
        val disabled = HashSet<KClass<*>>()
        for(element in sorted){
            if(!element.canLoad() || element.findAnnotation<Module>()!!.depend.any{it in disabled})
                disabled += element
            else
                instances += ModuleManager.createModule(element, modulePlugin[element]!!)
        }

        for(element in instances)
            if(element.state != IModule.State.ERROR)
                element.load()

        for(element in instances)
            if(element.state != IModule.State.ERROR)
                element.enable()
    }

    override fun onShutdown() {
        for(element in instances.reversed())
            if(element.state != IModule.State.ERROR)
                element.disable()
    }
}