/*
 * Copyright 2020 Spikot project authors
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
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@PublishedApi
internal class ListConfigProperty<T : Any>(
    private val type: KClass<T>,
    private val name: String? = null
) : ReadWriteProperty<ConfigSpec, MutableList<T>> {
    private var cache: MutableList<T> = mutableListOf()
    private var cached: Boolean = false
    override fun getValue(thisRef: ConfigSpec, property: KProperty<*>): MutableList<T> {
        if (!cached) {
            @Suppress("UNCHECKED_CAST")
            val value = thisRef.yaml.getListByType("${thisRef.path}.${name ?: property.name}", type) as MutableList<T>?
            if (value == null) {
                cache = mutableListOf()
                thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", cache)
            } else {
                cache = value
            }
            cached = true
        }
        return cache
    }

    override fun setValue(thisRef: ConfigSpec, property: KProperty<*>, value: MutableList<T>) {
        cache = value
        cached = true
        thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", value)
    }
}