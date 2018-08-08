package io.github.ReadyMadeProgrammer.Spikot.modules

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.koin.dsl.context.ParameterProvider
import org.koin.dsl.module.applicationContext
import org.koin.standalone.inject

@ExternalModule
class PluginModule : ModuleConfig {
    override val module = applicationContext {
        factory { pv: ParameterProvider -> Bukkit.getPluginManager().getPlugin(pv["name"]) }
    }
}

fun <T : Plugin> Component.injectPlugin(name: String) = inject<Plugin> { mapOf("name" to name) }