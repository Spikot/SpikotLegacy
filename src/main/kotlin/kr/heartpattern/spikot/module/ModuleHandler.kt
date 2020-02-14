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

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.misc.AbstractMutableProperty
import kr.heartpattern.spikot.utils.getInstance
import mu.KotlinLogging
import kotlin.reflect.KClass

/**
 * Handle module's whole lifecycle
 */
class ModuleHandler(val type: KClass<*>, val owner: SpikotPlugin, created: IModule? = null) {
    constructor(owner: SpikotPlugin, created: IModule) : this(created::class, owner, created)
    companion object{
        @JvmStatic
        private val logger = KotlinLogging.logger{}
    }

    internal object MutableModuleHandlerProperty : AbstractMutableProperty<ModuleHandler>(IModule.ModuleHandlerProperty)
    internal object MutableStateProperty : AbstractMutableProperty<IModule.State>(IModule.StateProperty)
    private object MutablePluginProperty: AbstractMutableProperty<SpikotPlugin>(IModule.PluginProperty)

    private val interceptors = ModuleManager.findInterceptor(type)
    /**
     * Handled module
     */
    var module: IModule? = null
        private set

    init {
        logger.debug { "Create module: ${type.simpleName}" }

        try {
            if (created != null && created.context[IModule.StateProperty] != null)
                throw IllegalStateException("Module is already loaded")

            module = created ?: type.getInstance() as IModule
            module!!.context[MutablePluginProperty] = owner
            module!!.context[MutableModuleHandlerProperty] = this

            if (created == null) {
                interceptors.forEach { interceptor ->
                    interceptor.onCreate(this)
                }
            }

            module!!.context[MutableStateProperty] = IModule.State.CREATE
        } catch (e: Throwable) {
            logger.error("Error occur while create module: ${type.simpleName}", e)
            interceptors.forEach { interceptor ->
                interceptor.onError(this, IModule.State.CREATE, e)
            }
            module?.context?.set(MutableStateProperty, IModule.State.ERROR)
        }
    }

    /**
     * Load module
     * @return true if load successfully
     */
    fun load(): Boolean {
        return performStep(IModule.State.CREATE, IModule.State.LOAD, IModule::onLoad, IModuleInterceptor::onLoad)
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
        module!!.context[MutableModuleHandlerProperty] = null
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
        val state = module?.context?.get(IModule.StateProperty)
        check(state == previous) { "Module should be $previous, but $state" }
        val newState = try {
            logger.debug { "${next.readable} module: ${module!!.javaClass.simpleName}" }
            module!!.task()

            interceptors.forEach { interceptor ->
                interceptor.intercept(this)
            }
            next
        } catch (e: Throwable) {
            logger.error(e) { "Error occur while ${next.readable} module: ${type.simpleName}" }
            interceptors.forEach { interceptor ->
                interceptor.onError(this, next, e)
            }
            IModule.State.ERROR
        }
        module?.context?.set(MutableStateProperty, newState)
        return state != IModule.State.ERROR
    }
}

fun IModule.create(plugin: SpikotPlugin) {
    ModuleHandler(plugin, this)
}

fun IModule.load() {
    context[IModule.ModuleHandlerProperty]!!.load()
}

fun IModule.enable() {
    context[IModule.ModuleHandlerProperty]!!.enable()
}

fun IModule.disable() {
    context[IModule.ModuleHandlerProperty]!!.disable()
}