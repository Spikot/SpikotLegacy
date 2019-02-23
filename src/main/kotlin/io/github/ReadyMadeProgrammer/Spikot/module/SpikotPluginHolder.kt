package io.github.ReadyMadeProgrammer.Spikot.module

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import kotlin.reflect.KClass

data class SpikotPluginHolder(
        val plugin: Spikot,
        val module: MutableSet<KClass<*>> = HashSet(),
        val command: MutableSet<KClass<*>> = HashSet(),
        val config: MutableSet<KClass<*>> = HashSet(),
        val serializer: MutableSet<KClass<*>> = HashSet(),
        val data: MutableSet<KClass<*>> = HashSet()
)