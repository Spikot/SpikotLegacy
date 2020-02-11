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

import com.google.common.collect.HashMultimap
import kr.heartpattern.spikot.module.ModulePriority.*
import kr.heartpattern.spikot.spikot
import java.util.*
import java.util.concurrent.atomic.LongAdder
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.superclasses

private object SystemMarkerModule : AbstractModule()
private object APIMarkerModule : AbstractModule()
private object LowestMarkerModule : AbstractModule()
private object LowMarkerModule : AbstractModule()
private object NormalMarkerModule : AbstractModule()
private object HighMarkerModule : AbstractModule()

private val markerSet = setOf(
    SystemMarkerModule::class,
    APIMarkerModule::class,
    LowestMarkerModule::class,
    LowMarkerModule::class,
    NormalMarkerModule::class,
    HighMarkerModule::class
)

internal fun sortModuleDependencies(modules: List<KClass<*>>): List<KClass<*>> {
    val dependencies = HashMultimap.create<KClass<*>, KClass<*>>()
    val counting = HashMap<KClass<*>, LongAdder>()

    dependencies.put(SystemMarkerModule::class, APIMarkerModule::class)
    dependencies.put(APIMarkerModule::class, LowestMarkerModule::class)
    dependencies.put(LowestMarkerModule::class, LowMarkerModule::class)
    dependencies.put(LowMarkerModule::class, NormalMarkerModule::class)
    dependencies.put(NormalMarkerModule::class, HighMarkerModule::class)

    counting.put(SystemMarkerModule::class, LongAdder())
    counting.getOrPut(APIMarkerModule::class, ::LongAdder).increment()
    counting.getOrPut(LowestMarkerModule::class, ::LongAdder).increment()
    counting.getOrPut(LowMarkerModule::class, ::LongAdder).increment()
    counting.getOrPut(NormalMarkerModule::class, ::LongAdder).increment()
    counting.getOrPut(HighMarkerModule::class, ::LongAdder).increment()

    for (module in modules) {
        counting.computeIfAbsent(module) { LongAdder() }
        val priority = module.findAnnotation<Module>()!!.priority
        val before = module
            .findAnnotation<Module>()!!
            .dependOn
            .toSet() + module
            .allSuperclasses
            .flatMap {
                it.findAnnotation<Module>()?.dependOn?.asIterable() ?: listOf()
            }
            .toSet()

        val after = (module
            .findAnnotation<LoadBefore>()
            ?.value
            ?.toSet()
            ?: setOf()) + module
            .allSuperclasses
            .flatMap {
                it.findAnnotation<LoadBefore>()?.value?.asIterable() ?: listOf()
            }
            .toSet()

        for (element in after) {
            dependencies.put(module, element)
            counting.getOrPut(element, ::LongAdder).increment()
        }

        for (element in before) {
            dependencies.put(element, module)
            counting.getOrPut(module, ::LongAdder).increment()
        }

        fun update(before: KClass<*>?, after: KClass<*>?) {
            if (before != null) {
                dependencies.put(before, module)
                counting.getOrPut(module, ::LongAdder).increment()
            }
            if (after != null) {
                dependencies.put(module, after)
                counting.getOrPut(after, ::LongAdder).increment()
            }
        }

        when (priority) {
            DEFAULT -> {
                if (after.isEmpty()) {
                    val inherit = module.superclasses.asSequence().map { it.findAnnotation<Module>() }.firstOrNull { it != null }
                    if (inherit == null) {
                        update(LowMarkerModule::class, null)
                    } else {
                        when (inherit.priority) {
                            SYSTEM -> {
                                update(null, SystemMarkerModule::class)
                            }
                            API -> {
                                update(SystemMarkerModule::class, APIMarkerModule::class)
                            }
                            LOWEST -> {
                                update(APIMarkerModule::class, LowestMarkerModule::class)
                            }
                            LOW -> {
                                update(LowestMarkerModule::class, LowMarkerModule::class)
                            }
                            NORMAL -> {
                                update(LowMarkerModule::class, NormalMarkerModule::class)
                            }
                            HIGH -> {
                                update(NormalMarkerModule::class, HighMarkerModule::class)
                            }
                            HIGHEST -> {
                                update(HighMarkerModule::class, null)
                            }
                            DEFAULT -> {
                                update(LowMarkerModule::class, null)
                            }
                        }
                    }
                }
            }
            SYSTEM -> {
                update(null, SystemMarkerModule::class)
            }
            API -> {
                update(SystemMarkerModule::class, APIMarkerModule::class)
            }
            LOWEST -> {
                update(APIMarkerModule::class, LowestMarkerModule::class)
            }
            LOW -> {
                update(LowestMarkerModule::class, LowMarkerModule::class)
            }
            NORMAL -> {
                update(LowMarkerModule::class, NormalMarkerModule::class)
            }
            HIGH -> {
                update(NormalMarkerModule::class, HighMarkerModule::class)
            }
            HIGHEST -> {
                update(HighMarkerModule::class, null)
            }
        }
    }

    val zeros = counting.asSequence()
        .filter { it.value.sum() == 0L }
        .map { it.key }
        .toList()

    val result = LinkedList<KClass<*>>()

    for (start in zeros) {
        val queue = LinkedList<KClass<*>>()
        queue += start

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result += current

            for (child in dependencies[current]) {
                val count = counting[child]!!
                count.decrement()
                if (count.sum() == 0L)
                    queue += child
            }
        }
    }

    if (result.size != modules.size + 6) {
        val diff = modules.filterNot { result.contains(it) }
        spikot.logger.error("Circular module dependency found. Following module produce conflict")
        for (module in diff) {
            spikot.logger.error(module.qualifiedName)
        }
        throw IllegalStateException("Circular module dependency found")
    }

    return result.filter { it !in markerSet }
}