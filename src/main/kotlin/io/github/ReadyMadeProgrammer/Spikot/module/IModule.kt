@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.module

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.event.Listener
import java.io.File

interface IModule : Listener {
    fun onLoad(plugin: Spikot) {}

    fun onEnable() {}

    fun onDisable() {}
}

abstract class AbstractModule : IModule {
    private lateinit var _plugin: Spikot
    private lateinit var _logger: KLogger
    protected val plugin: Spikot
        get() = _plugin

    protected val logger: KLogger
        get() = _logger

    protected fun file(name: String): File {
        return File(_plugin.dataFolder, name)
    }

    override fun onLoad(plugin: Spikot) {
        _plugin = plugin
        _logger = KotlinLogging.logger(plugin.name)
    }
}