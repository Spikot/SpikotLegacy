package kr.heartpattern.spikot.plugin

import kr.heartpattern.spikot.SpikotPlugin
import kotlin.reflect.KClass

data class PluginWrapper(
    val plugin: SpikotPlugin,
    val classes: MutableMap<KClass<*>, MutableSet<KClass<*>>> = HashMap()
)