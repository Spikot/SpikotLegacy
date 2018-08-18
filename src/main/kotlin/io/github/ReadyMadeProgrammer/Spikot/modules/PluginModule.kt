package io.github.ReadyMadeProgrammer.Spikot.modules

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.koin.standalone.inject

@ExternalModule
class PluginModule : ModuleConfig {
    override val module = org.koin.dsl.module.module {
        Bukkit.getPluginManager().plugins.forEach { pl ->
            factory(name = pl.javaClass.simpleName) { pl }
        }
    }
}

inline fun <reified T : Plugin> Component.injectPlugin(): Lazy<T> = lazy { inject<Plugin>(T::class.java.simpleName).value as T }