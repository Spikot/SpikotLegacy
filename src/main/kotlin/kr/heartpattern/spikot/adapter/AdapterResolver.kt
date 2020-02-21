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

package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.*
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Adapter resolver that select which adapter to be used by default
 */
@BaseModule
@ServerModule(priority = ModulePriority.API)
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