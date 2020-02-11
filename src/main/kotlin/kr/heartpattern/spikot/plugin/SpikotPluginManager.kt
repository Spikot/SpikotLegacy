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

package kr.heartpattern.spikot.plugin

import com.github.salomonbrys.kotson.forEach
import com.google.gson.JsonParser
import kr.heartpattern.spikot.IBootstrap
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.utils.catchAll
import mu.KotlinLogging
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarFile
import kotlin.collections.HashSet
import kotlin.reflect.KClass

/**
 * Manage all spikot plugin
 */
object SpikotPluginManager : IBootstrap {
    private val logger = KotlinLogging.logger {}
    @PublishedApi
    internal val plugins = HashSet<PluginWrapper>()

    override fun onStartup() {
        Bukkit.getPluginManager().plugins.asSequence()
            .filter { it is SpikotPlugin }
            .forEach { p ->
                logger.debug("Find plugin: ${p.name}")
                logger.catchAll("Cannot load plugin: ${p.name}") {
                    val field = JavaPlugin::class.java.getDeclaredField("file")
                    field.isAccessible = true
                    val file = field[p] as File
                    field.isAccessible = false
                    val jarFile = JarFile(file)
                    val wrapper = PluginWrapper(p as SpikotPlugin)
                    jarFile.entries().iterator().forEach entryIterate@{ entry ->
                        if (!entry.name.startsWith("spikot") || !entry.name.endsWith(".json"))
                            return@entryIterate
                        val json = JsonParser().parse(jarFile.getInputStream(entry).bufferedReader()).asJsonObject
                        json.forEach { key, value ->
                            logger.catchAll("Cannot load annotation locator of plugin: ${p.name}") {
                                val annotation = Class.forName(key).kotlin
                                val set = wrapper.classes.getOrPut(annotation) { HashSet() }
                                val classes = value.asJsonArray
                                for (clazz in classes) {
                                    set.add(Class.forName(clazz.asString).kotlin)
                                }
                                wrapper.classes[annotation] = set
                            }
                        }
                    }
                    if (wrapper.classes.isNotEmpty()) {
                        plugins += wrapper
                    }
                    jarFile.close()
                }
            }
    }

    /**
     * Iterate all class annotated with T. T should be annotated with FindAnnotation annotation
     * @param T Annotation to find
     * @param block Consumer
     */
    inline fun <reified T : Annotation> forEachAnnotation(block: (AnnotatedClass<T>) -> Unit) {
        forEachAnnotation(T::class, block)
    }

    /**
     * Iterate all class annotated with T. T should be annotated with FindAnnotation annotation
     * @param type Annotation to find
     * @param block Consumer
     */
    inline fun <T : Annotation> forEachAnnotation(type: KClass<T>, block: (AnnotatedClass<T>) -> Unit) {
        for (plugin in plugins) {
            val classes = plugin.classes[type]
            if (classes != null) {
                for (clazz in classes) {
                    @Suppress("UNCHECKED_CAST")
                    block(AnnotatedClass(clazz, plugin.plugin, clazz.annotations.first{type.isInstance(it)} as T))
                }
            }
        }
    }

    /**
     * Return iterator of all class annotated with T. T should be annotated with FindAnnotation annotation
     * @param T Annotation to find
     * @return Iterator of all class annotated with T
     */
    inline fun <reified T : Annotation> annotationIterator(): Iterator<AnnotatedClass<T>> {
        val list = LinkedList<AnnotatedClass<T>>()
        forEachAnnotation<T> { annotatedClass ->
            list += annotatedClass
        }
        return list.iterator()
    }
}

/**
 * Class information annotated with T
 * @param type Class annotated with T
 * @param plugin Plugin that owing [type] class
 * @param annotation Annotation instance
 */
data class AnnotatedClass<T>(
    val type: KClass<*>,
    val plugin: SpikotPlugin,
    val annotation: T
)