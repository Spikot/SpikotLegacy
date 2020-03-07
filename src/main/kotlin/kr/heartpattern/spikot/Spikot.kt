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

package kr.heartpattern.spikot

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.module.SingletonModuleManager
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import mu.KotlinLogging
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File

/**
 * Spikot plugin instance used in spikot framework
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@PublishedApi
internal lateinit var spikot: Spikot

/**
 * Spikot plugin main.
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@Plugin(name = "Spikot", version = "4.0.1-SNAPSHOT")
@Dependency(plugin = "ProtocolLib")
class Spikot : SpikotPlugin() {
    val spikotLogger = KotlinLogging.logger{}
    internal val enabled = HashSet<String>()

    override fun onLoad() {
        BootstrapManager.onLoad()
    }

    override fun onEnable() {
        spikot = this
        object : BukkitRunnable() {
            override fun run() {
                spikotLogger.info { "Start loading spikot" }
                spikotLogger.catchAll("Cannot load features") {
                    val feature = YamlConfiguration.loadConfiguration(File(spikot.dataFolder, "module.yml"))
                    enabled += feature.getStringList("enable")
                }

                SpikotPluginManager.onStartup()
                BootstrapManager.onStartup()
                spikotLogger.info { "End loading spikot" }
            }
        }.runTaskLater(this, 1L)
    }

    override fun onDisable() {
        spikotLogger.info { "Start disable spikot" }
        BootstrapManager.onShutdown()
        spikotLogger.info("End disable spikot")
    }

    inline fun <reified T : IModule> get(): T {
        return SingletonModuleManager.typeHandlerMap[T::class] as? T
            ?: error("Cannot find loaded module: ${T::class.simpleName}")
    }
}