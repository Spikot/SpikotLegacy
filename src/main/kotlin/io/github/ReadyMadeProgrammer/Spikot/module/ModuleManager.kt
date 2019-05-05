@file:Suppress("UNCHECKED_CAST")

package io.github.ReadyMadeProgrammer.Spikot.module

import io.github.ReadyMadeProgrammer.Spikot.event.subscribe
import io.github.ReadyMadeProgrammer.Spikot.logger
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import io.github.ReadyMadeProgrammer.Spikot.utils.getInstance
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.full.findAnnotation

internal object ModuleManager {
    internal val enabled = HashSet<String>()
    internal lateinit var instances: List<Pair<SpikotPluginHolder, IModule>>

    @Suppress("SpellCheckingInspection")
    fun load() {
        val feature = YamlConfiguration.loadConfiguration(File(spikotPlugin.dataFolder, "module.yml"))
        enabled += feature.getStringList("enable")
    }

    fun start() {
        instances = SpikotPluginManager.spikotPlugins
                .asSequence()
                .flatMap { p -> p.module.asSequence().map { Pair(p, it) } }
                .filter { (_, m) ->
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

        instances.forEach { (holder, module) ->
            catchAll {
                module.onLoad(holder.plugin)
            }
        }
        instances.forEach { (holder, module) ->
            catchAll {
                module.onEnable()
                holder.plugin.subscribe(module)
            }
        }
    }

    fun end() {
        instances.toList().reversed().forEach { (_, module) ->
            module.onDisable()
        }
    }
}