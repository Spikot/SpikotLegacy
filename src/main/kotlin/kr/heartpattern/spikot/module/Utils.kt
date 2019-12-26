package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.spikot
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

internal fun KClass<*>.canLoad(): Boolean {
    val feature = findAnnotation<Feature>()
    val disable = findAnnotation<Disable>()
    if (disable != null) return false
    if (feature == null) return true
    return spikot.enabled.contains(feature.value)
}

/**
 * Check feature is enabled
 * @param feature Feature name
 * @return Whether feature is enabled
 */
fun isFeatureEnabled(feature: String): Boolean {
    return spikot.enabled.contains(feature)
}

/**
 * Run lambda only if feature is enabled
 * @param feature Feature name
 * @param runnable lambda which is run only if feature is enabled
 */
inline fun onEnabled(feature: String, runnable: () -> Unit) {
    if (isFeatureEnabled(feature)) {
        runnable()
    }
}