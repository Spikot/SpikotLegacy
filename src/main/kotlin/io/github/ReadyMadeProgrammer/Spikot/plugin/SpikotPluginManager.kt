package io.github.ReadyMadeProgrammer.Spikot.plugin

import com.github.salomonbrys.kotson.forEach
import com.google.gson.JsonParser
import io.github.ReadyMadeProgrammer.Spikot.Spikot
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarFile
import kotlin.collections.HashSet
import kotlin.reflect.KClass

object SpikotPluginManager {
    val plugins = HashSet<PluginWrapper>()
    internal fun load() {
        Bukkit.getPluginManager().plugins.asSequence()
            .filter { it is Spikot }
            .forEach { p ->
                catchAll {
                    val field = JavaPlugin::class.java.getDeclaredField("file")
                    field.isAccessible = true
                    val file = field[p] as File
                    field.isAccessible = false
                    val jarFile = JarFile(file)
                    val wrapper = PluginWrapper(p as Spikot)
                    jarFile.entries().iterator().forEach entryIterate@{ entry ->
                        if (!entry.name.startsWith("spikot"))
                            return@entryIterate
                        val json = JsonParser().parse(jarFile.getInputStream(entry).bufferedReader()).asJsonObject
                        json.forEach { key, value ->
                            catchAll {
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

    inline fun <reified T> forEach(block: (Spikot, KClass<*>) -> Unit) {
        for (plugin in plugins) {
            val classes = plugin.classes[T::class]
            if (classes != null) {
                for (clazz in classes) {
                    block(plugin.plugin, clazz)
                }
            }
        }
    }

    inline fun <reified T> iterator(): Iterator<Pair<Spikot, KClass<*>>> {
        val list = LinkedList<Pair<Spikot, KClass<*>>>()
        forEach<T> { plugin, clazz ->
            list += Pair(plugin, clazz)
        }
        return list.iterator()
    }
}