package io.github.ReadyMadeProgrammer.Spikot.module

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

internal fun KClass<*>.canLoad(): Boolean {
    val feature = findAnnotation<Feature>()
    val disable = findAnnotation<Disable>()
    if (disable != null) return false
    if (feature == null) return true
    return ModuleManager.enabled.contains(feature.value)
}

fun isFeatureEnabled(feature: String): Boolean {
    return ModuleManager.enabled.contains(feature)
}