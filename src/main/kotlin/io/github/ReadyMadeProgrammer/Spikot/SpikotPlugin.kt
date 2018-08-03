package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.command.CommandManager
import io.github.ReadyMadeProgrammer.Spikot.modules.DIResolver
import io.github.ReadyMadeProgrammer.Spikot.utils.KPlayerListener
import io.github.ReadyMadeProgrammer.Spikot.utils.initTaskChain
import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.reflect.full.createInstance

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
class SpikotPlugin: JavaPlugin(){
    private val featureFile = File(dataFolder.parent, "feature.txt")
    override fun onEnable(){
        spikotLogger.info { "Start loading spikot" }
        spikotPlugin = this
        KPlayerListener.start(this)
        initTaskChain(this)
        featureFile.readLines().forEach { DIResolver.feature.add(it) }
        DIResolver.load()
        DIResolver.modules.forEach {
            val instance = it.createInstance()
            instance.onStart()
            DIResolver.moduleInstances.add(instance)
        }
        CommandManager.loadCommand()
        spikotLogger.info { "End loading spikot" }
    }

    override fun onDisable() {
        spikotLogger.info { "Start disable spikot" }
        DIResolver.moduleInstances.forEach {
            it.onStop()
        }
        spikotLogger.info("End disable spikot")
    }
}