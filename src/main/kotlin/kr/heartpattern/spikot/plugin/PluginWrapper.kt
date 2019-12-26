package kr.heartpattern.spikot.plugin

import kr.heartpattern.spikot.SpikotPlugin
import kotlin.reflect.KClass

@PublishedApi
internal data class PluginWrapper(
    val plugin: SpikotPlugin,
    val classes: MutableMap<KClass<*>, MutableSet<KClass<*>>> = HashMap()
)