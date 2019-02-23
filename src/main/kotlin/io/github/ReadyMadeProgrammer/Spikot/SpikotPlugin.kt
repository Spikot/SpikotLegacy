package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.module.ModuleManager
import io.github.ReadyMadeProgrammer.Spikot.module.SpikotPluginManager
import mu.KotlinLogging
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
internal lateinit var spikotPlugin: SpikotPlugin

/**
 * Spikot plugin main.
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@Plugin(name = "Spikot", version = "2.1.2")
class SpikotPlugin : Spikot() {
    override fun onEnable() {
        println(Thread.currentThread().contextClassLoader)
        spikotLogger.info { "Start loading spikot" }
        spikotPlugin = this
        ModuleManager.load()
        SpikotPluginManager.load()
        object : BukkitRunnable() {
            override fun run() {
                ModuleManager.start()
            }
        }.runTask(this)
        spikotLogger.info { "End loading spikot" }
    }

    override fun onDisable() {
        spikotLogger.info { "Start disable spikot" }
        ModuleManager.end()
        spikotLogger.info("End disable spikot")
    }
}