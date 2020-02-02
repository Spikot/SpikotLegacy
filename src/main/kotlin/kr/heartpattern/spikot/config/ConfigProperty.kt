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

package kr.heartpattern.spikot.config

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

internal class ConfigProperty<T>(
    private val default: T,
    private val name: String? = null
) : ReadWriteProperty<ConfigSpec, T> {
    private var cache: T? = null
    private var cached = false

    override fun getValue(thisRef: ConfigSpec, property: KProperty<*>): T {
        if (!cached) {
            @Suppress("UNCHECKED_CAST")
            cache = thisRef.yaml.getByType("${thisRef.path}.${name
                ?: property.name}", property.returnType.jvmErasure) as T?
            if (cache == null) {
                thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", default)
            }
            cached = true
        }
        return cache ?: default
    }

    override fun setValue(thisRef: ConfigSpec, property: KProperty<*>, value: T) {
        thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", value)
        cache = value
        cached = true
    }
}