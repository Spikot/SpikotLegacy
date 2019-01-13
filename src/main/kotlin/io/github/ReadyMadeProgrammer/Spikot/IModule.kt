@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot

import mu.KLogger
import mu.KotlinLogging
import org.bukkit.event.Listener
import java.io.File
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class Module

@Module
interface IModule : Listener {
    fun onLoad(plugin: Spikot) {
    }

    @Deprecated(message = "To get plugin, use onLoad(plugin: Spikot)")
    fun onEnable(plugin: Spikot) {

    }

    fun onEnable() {

    }

    fun onDisable() {

    }
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