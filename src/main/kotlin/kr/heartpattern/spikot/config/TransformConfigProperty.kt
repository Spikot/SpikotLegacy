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

import kr.heartpattern.spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@PublishedApi
internal class TransformConfigProperty<T : Any, R : Any>(private val rawType: KClass<T>, private val default: R, private val converter: Converter<T, R>) : ReadWriteProperty<ConfigSpec, R> {
    private var cache: R? = null
    private var cached = false
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: ConfigSpec, property: KProperty<*>): R {
        if (!cached) {
            val raw = thisRef.yaml.getByType("${thisRef.path}.${property.name}", rawType)
            if (raw != null) {
                cache = converter.read(raw as T)
            }
            cached = true
        }
        return cache ?: default
    }

    override fun setValue(thisRef: ConfigSpec, property: KProperty<*>, value: R) {
        thisRef.yaml.set(property.name, converter.write(value))
        cache = value
        cached = true
    }
}