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
import kotlin.reflect.full.findAnnotation

object SpikotPluginManager : IBootstrap {
    private val logger = KotlinLogging.logger {}
    val plugins = HashSet<PluginWrapper>()
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
                        if (!entry.name.startsWith("spikot"))
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

    inline fun <reified T : Annotation> forEachAnnotation(block: (AnnotatedClass<T>) -> Unit) {
        forEachAnnotation(T::class, block)
    }

    inline fun <reified T : Annotation> forEachAnnotation(type: KClass<T>, block: (AnnotatedClass<T>) -> Unit) {
        for (plugin in plugins) {
            val classes = plugin.classes[type]
            if (classes != null) {
                for (clazz in classes) {
                    block(AnnotatedClass(clazz, plugin.plugin, clazz.findAnnotation()!!))
                }
            }
        }
    }

    inline fun <reified T : Annotation> annotationIterator(): Iterator<AnnotatedClass<T>> {
        val list = LinkedList<AnnotatedClass<T>>()
        forEachAnnotation<T> { annotatedClass ->
            list += annotatedClass
        }
        return list.iterator()
    }
}

data class AnnotatedClass<T>(
    val type: KClass<*>,
    val plugin: SpikotPlugin,
    val annotation: T
)