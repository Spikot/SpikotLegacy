package io.github.ReadyMadeProgrammer.Spikot

import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin

internal val logger = KotlinLogging.logger("Spikot")
internal lateinit var spikotPlugin: SpikotPlugin


class SpikotPlugin: JavaPlugin(){

    override fun onEnable(){
        spikotPlugin = this
        KPlayerListener.start(this)
        ModuleManager.addModuleLifeCycle(ServerScope::class, ServerLifeCycle)
        ModuleManager.load()
        initTaskChain(this)
    }

    override fun onDisable() {
        ServerLifeCycle.destroy()
    }
}