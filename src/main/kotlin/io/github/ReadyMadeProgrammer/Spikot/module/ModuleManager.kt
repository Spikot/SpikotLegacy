@file:Suppress("UNCHECKED_CAST")

package io.github.ReadyMadeProgrammer.Spikot.module

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import io.github.ReadyMadeProgrammer.Spikot.event.subscribe
import io.github.ReadyMadeProgrammer.Spikot.logger
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import io.github.ReadyMadeProgrammer.Spikot.utils.getInstance
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.full.findAnnotation

internal object ModuleManager {
    internal val enabled = HashSet<String>()
    internal lateinit var instances: List<Pair<Spikot, IModule>>

    @Suppress("SpellCheckingInspection")
    fun load() {
        val feature = YamlConfiguration.loadConfiguration(File(spikotPlugin.dataFolder, "module.yml"))
        enabled += feature.getStringList("enable")
    }

    fun start() {
        instances = SpikotPluginManager
                .iterator<Module>()
                .asSequence()
                .filter { (_, m) ->
                    onDebug {
                        logger.info("Find module: ${m.simpleName}")
                    }
                    val result = m.findAnnotation<Module>() != null
                    if (!result) {
                        logger.warn("Cannot load module: ${m.simpleName}")
                    }
                    result && m.canLoad()
                }
                .sortedBy { it.second.findAnnotation<Module>()!!.loadOrder }
                .map { (holder, module) -> Pair(holder, module.getInstance() as? IModule?) }
                .filter { (_, module) -> module != null }
                .map { (holder, module) -> Pair(holder, module as IModule) }
                .toList()

        instances.forEach { (plugin, module) ->
            catchAll {
                onDebug {
                    logger.info("Load module: ${module.javaClass.simpleName}")
                }
                module.onLoad(plugin)
            }
        }
        instances.forEach { (plugin, module) ->
            catchAll {
                onDebug {
                    logger.info("Enable module: ${module.javaClass.simpleName}")
                }
                module.onEnable()
                onDebug {
                    logger.info("Subscribe module: ${module.javaClass.simpleName}")
                }
                plugin.subscribe(module)
            }
        }
    }

    fun end() {
        instances.toList().reversed().forEach { (_, module) ->
            onDebug {
                logger.info("Disable module: ${module.javaClass.simpleName}")
            }
            module.onDisable()
        }
    }
}