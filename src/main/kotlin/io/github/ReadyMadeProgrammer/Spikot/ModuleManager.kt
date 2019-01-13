@file:Suppress("UNCHECKED_CAST")

package io.github.ReadyMadeProgrammer.Spikot

import com.esotericsoftware.yamlbeans.YamlReader
import io.github.ReadyMadeProgrammer.Spikot.command.CommandHandler
import io.github.ReadyMadeProgrammer.Spikot.command.CommandManager
import io.github.ReadyMadeProgrammer.Spikot.gson.GsonManager
import io.github.ReadyMadeProgrammer.Spikot.gson.GsonSerializer
import io.github.ReadyMadeProgrammer.Spikot.i18n.loadKey
import io.github.ReadyMadeProgrammer.Spikot.persistence.DataManager
import io.github.ReadyMadeProgrammer.Spikot.persistence.PlayerDataManager
import org.bukkit.Bukkit
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal object ModuleManager {
    private val modules: MutableMap<String, Set<IModule>> = mutableMapOf()
    @Suppress("SpellCheckingInspection")
    fun load() {
        spikotPlugin.dataFolder.parentFile.listFiles().asSequence()
                .filter { it.name.endsWith(".jar") }
                .map { JarFile(it) }
                .forEach {
                    try {
                        val set = mutableSetOf<IModule>()
                        if (it.getJarEntry("modules") != null) {
                            it.getInputStream(it.getJarEntry("modules")).bufferedReader().lines().forEach { name ->
                                val clazz = Class.forName(name).kotlin
                                set += (clazz.objectInstance ?: clazz.createInstance()) as IModule
                            }
                            it.getInputStream(it.getJarEntry("commands")).bufferedReader().lines().forEach { name ->
                                CommandManager.add(Class.forName(name).kotlin as KClass<out CommandHandler>)
                            }
                            val plugin = (YamlReader(it.getInputStream(it.getJarEntry("plugin.yml")).bufferedReader()).read() as Map<*, *>)["name"] as String
                            modules[plugin] = set
                        }
                        if (it.getJarEntry("messages") != null) {
                            it.getInputStream(it.getJarEntry("messages")).bufferedReader().lines().forEach { name ->
                                loadKey(Class.forName(name).kotlin)
                            }
                        }
                        if (it.getJarEntry("serializers") != null) {
                            val serializerSet = HashSet<KClass<out GsonSerializer<*>>>()
                            it.getInputStream(it.getJarEntry("serializers")).bufferedReader().lines().forEach { name ->
                                serializerSet.add(Class.forName(name).kotlin as KClass<out GsonSerializer<*>>)
                            }
                            GsonManager.initialize(serializerSet)
                        }
                        if (it.getJarEntry("playerdatas") != null) {
                            val playerDataSet = HashSet<KClass<*>>()
                            it.getInputStream(it.getJarEntry("playerdatas")).bufferedReader().lines().forEach { name ->
                                playerDataSet.add(Class.forName(name).kotlin)
                            }
                            PlayerDataManager.setup(playerDataSet)
                        }
                        if (it.getJarEntry("plugindatas") != null) {
                            val pluginDataSet = HashSet<KClass<*>>()
                            it.getInputStream(it.getJarEntry("plugindatas")).bufferedReader().lines().forEach { name ->
                                pluginDataSet.add(Class.forName(name).kotlin)
                            }
                            DataManager.initialize(pluginDataSet)
                        }
                        it.close()
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
    }

    fun start() {
        PlayerDataManager.onServerOpen()
        Bukkit.getPluginManager().registerEvents(PlayerDataManager, spikotPlugin)
        modules.forEach { (plugin, m) ->
            val sPlugin = Bukkit.getPluginManager().getPlugin(plugin) as Spikot
            m.forEach {
                it.onLoad(sPlugin)
            }
            m.forEach {
                safe {
                    @Suppress("DEPRECATION")
                    it.onEnable(sPlugin)
                } //Legacy
                safe { it.onEnable() }
                Bukkit.getPluginManager().registerEvents(it, sPlugin)
            }
        }
    }

    fun end() {
        modules.forEach { (_, m) ->
            m.forEach {
                safe { it.onDisable() }
            }
        }
        PlayerDataManager.onServerStop()
        DataManager.save()
    }
}

private inline fun safe(run: () -> Unit) {
    try {
        run()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}