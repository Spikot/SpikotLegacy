package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.module.ModuleManager
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import mu.KotlinLogging
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * Internal logger used in spikot framework
 */
@Suppress("unused")
internal val logger = KotlinLogging.logger("Spikot")
private val spikotLogger = logger
/**
 * Spikot plugin instance used in spikot framework
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@PublishedApi
internal lateinit var spikotPlugin: SpikotPlugin

/**
 * Spikot plugin main.
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@Plugin(name = "Spikot", version = "3.0.0-b21")
@Dependency(plugin = "ProtocolLib")
class SpikotPlugin : Spikot() {
    override fun onEnable() {
        spikotLogger.info { "Start loading spikot" }
        spikotPlugin = this
        object : BukkitRunnable() {
            override fun run() {
                SpikotPluginManager.load()
                ModuleManager.load()
                ModuleManager.start()
            }
        }.runTaskLater(this, 1L)
        spikotLogger.info { "End loading spikot" }
    }

    override fun onDisable() {
        spikotLogger.info { "Start disable spikot" }
        ModuleManager.end()
        spikotLogger.info("End disable spikot")
    }
}