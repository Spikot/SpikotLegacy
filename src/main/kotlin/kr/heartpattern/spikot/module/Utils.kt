/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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