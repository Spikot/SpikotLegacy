package io.github.ReadyMadeProgrammer.Spikot.plugin

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import kotlin.reflect.KClass

data class PluginWrapper(
        val plugin: Spikot,
        val classes: MutableMap<KClass<*>, MutableSet<KClass<*>>> = HashMap()
)