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
internal lateinit var spikot: Spikot

/**
 * Spikot plugin main.
 * @since 1.0.0
 * @author ReadyMadeProgrammer
 */
@Plugin(name = "Spikot", version = "4.0.0-SNAPSHOT")
@Dependency(plugin = "ProtocolLib")
class Spikot : SpikotPlugin() {
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
        return SingletonModuleManager.instances[T::class] as? T
            ?: error("Cannot find loaded module: ${T::class.simpleName}")
    }
}