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

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.Bootstrap
import kr.heartpattern.spikot.IBootstrap
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.getInstance
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf

/**
 * Manage all modules. [ModuleHandler]
 */
@Bootstrap(-100000)
object ModuleManager : IBootstrap {
    private lateinit var moduleInterceptor: Set<Pair<IModuleInterceptor, ModuleInterceptor>>
    private val shutdown = LinkedList<ModuleHandler>()

    /**
     * Initialize module manager
     */
    override fun onLoad() {
        moduleInterceptor = SpikotPluginManager
            .annotationIterator<ModuleInterceptor>()
            .asSequence()
            .map { (type) ->
                type.getInstance() as IModuleInterceptor to
                    type.findAnnotation<ModuleInterceptor>()!!
            }
            .toSet()
    }

    /**
     * Shutdown module manager
     */
    override fun onShutdown() {
        for (handler in shutdown)
            if (handler.module?.context?.get(IModule.StateProperty) == IModule.State.ENABLE)
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

    /**
     * Set module to disable on shutdown
     */
    fun disableOnShutdown(module: IModule) {
        shutdown.add(module.context[IModule.ModuleHandlerProperty]
            ?: throw IllegalArgumentException("Module $module does not loaded"))
    }
}