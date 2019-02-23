package io.github.ReadyMadeProgrammer.Spikot.module

import com.github.salomonbrys.kotson.contains
import com.google.gson.JsonParser
import io.github.ReadyMadeProgrammer.Spikot.Spikot
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarFile

internal object SpikotPluginManager {
    internal val spikotPlugins = HashSet<SpikotPluginHolder>()

    fun load() {
        val loading = mapOf(
                "module" to SpikotPluginHolder::module,
                "command" to SpikotPluginHolder::command,
                "config" to SpikotPluginHolder::config,
                "serializer" to SpikotPluginHolder::serializer,
                "data" to SpikotPluginHolder::data
        )

        Bukkit.getPluginManager().plugins.asSequence()
                .filter { it is Spikot }
                .forEach { p ->
                    catchAll {
                        val field = JavaPlugin::class.java.getDeclaredField("file")
                        field.isAccessible = true
                        val file = field[p] as File
                        field.isAccessible = false
                        val jarFile = JarFile(file)
                        val entry = jarFile.getJarEntry("spikot.json")
                        if (entry != null) {
                            val json = JsonParser().parse(jarFile.getInputStream(entry).bufferedReader()).asJsonObject
                            val holder = SpikotPluginHolder(p as Spikot)
                            loading.forEach { key, value ->
                                val acc = value.get(holder)
                                if (json.contains(key)) {
                                    val arr = json[key].asJsonArray
                                    arr.forEach {
                                        catchAll {
                                            acc.add(Class.forName(it.asString).kotlin)
                                        }
                                    }
                                }
                            }
                            spikotPlugins.add(holder)
                        }
                        jarFile.close()
                    }
                }
    }
}