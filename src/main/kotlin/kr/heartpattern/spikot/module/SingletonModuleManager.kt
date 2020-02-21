/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.Bootstrap
import kr.heartpattern.spikot.IBootstrap
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import mu.KotlinLogging
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@PublishedApi
@Bootstrap
internal object SingletonModuleManager : IBootstrap {
    private val logger = KotlinLogging.logger {}

    @PublishedApi
    internal val typeHandlerMap: MutableMap<KClass<*>, ModuleHandler> = HashMap()
    private val instances = LinkedList<ModuleHandler>()

    override fun onStartup() {
        val modules = LinkedList<KClass<*>>()
        val modulePlugin = HashMap<KClass<*>, SpikotPlugin>()

        for ((module, plugin) in SpikotPluginManager.annotationIterator<ServerModule>()) {
            if (module.findAnnotation<BaseModule>() != null)
                continue
            logger.debug("Find module: ${module.simpleName}")
            modulePlugin[module] = plugin
            modules += module
        }

        for ((module, plugin) in SpikotPluginManager.annotationIterator<Module>()) {
            if (module.findAnnotation<BaseModule>() != null)
                continue
            logger.debug("Find module: ${module.simpleName}")
            modulePlugin[module] = plugin
            modules += module
        }

        val sorted = sortModuleDependencies(modules)
        val disabled = HashSet<KClass<*>>()
        for (element in sorted) {
            if (!element.canLoad() || element.getModuleInfo()!!.dependOn.any { it in disabled })
                disabled += element
            else
                instances += ModuleManager.createModule(element, modulePlugin[element]!!)
        }

        for (element in instances)
            if (element.context[ModuleHandler.MutableStateProperty] != IModule.State.ERROR)
                element.load()

        for (element in instances)
            if (element.context[ModuleHandler.MutableStateProperty] != IModule.State.ERROR)
                element.enable()
    }

    override fun onShutdown() {
        for (element in instances.reversed())
            if (element.context[ModuleHandler.MutableStateProperty] != IModule.State.ERROR)
                element.disable()
    }
}

internal data class ModuleInfo(val priority: ModulePriority, val dependOn: Array<KClass<*>>)

internal fun KClass<*>.getModuleInfo(): ModuleInfo? {
    val module = findAnnotation<Module>()
    val serverModule = findAnnotation<ServerModule>()
    return when {
        serverModule != null -> ModuleInfo(serverModule.priority, serverModule.dependOn)
        module != null -> ModuleInfo(module.priority, module.dependOn)
        else -> null
    }
}