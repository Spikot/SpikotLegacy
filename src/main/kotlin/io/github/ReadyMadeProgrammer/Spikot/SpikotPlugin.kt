package io.github.ReadyMadeProgrammer.Spikot

import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin

class SpikotPlugin: JavaPlugin(){
    val logger = KotlinLogging.logger("Spikot")
    override fun onEnable(){
        KPlayerListener.start(this)

    }
}