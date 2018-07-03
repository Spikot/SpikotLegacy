package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.utils.KPlayerListener
import io.github.ReadyMadeProgrammer.Spikot.utils.initTaskChain
import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin

/**
 * Internal logger used in spikot framework
 */
internal val logger = KotlinLogging.logger("Spikot")

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

    override fun onEnable(){
        spikotPlugin = this
        KPlayerListener.start(this)
        initTaskChain(this)
    }

    override fun onDisable() {
    }
}