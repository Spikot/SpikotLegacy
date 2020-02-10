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

package kr.heartpattern.spikot.misc

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Immutable property
 * @param T Type of value for this property
 */
interface Property<T> {
    val key: String
        get() = this::class.qualifiedName!!
}

/**
 * Mutable property
 */
interface MutableProperty<T> : Property<T>

/**
 * Mutable property which point same thing with given property
 * @param property Property that is equivalent to this property
 */
abstract class AbstractMutableProperty<T>(private val property: Property<T>) : MutableProperty<T> {
    override val key: String
        get() = property.key
}

/**
 * Immutable property which represent boolean value
 */
interface FlagProperty : Property<Unit>

/**
 * Mutable property with represent boolean value
 */
interface MutableFlagProperty : MutableProperty<Unit>, FlagProperty

/**
 * Simple map which can store property-value pair
 */
@Suppress("UNCHECKED_CAST")
open class PropertyMap {
    protected val backingMap = HashMap<String, Any?>()

    /**
     * Get value of given property
     * @param property Property to get
     * @return Value correspond to given property. Null if not exists.
     */
    operator fun <T> get(property: Property<T>): T? {
        return backingMap[property.key] as T?
    }

    /**
     * Get value of given flag property
     * @param property Property to get
     * @return Value correspond to given property
     */
    operator fun get(property: FlagProperty): Boolean {
        return backingMap.contains(property.key)
    }

    /**
     * Check property is in map
     * @param property Property to check
     * @return Whether this map contains property
     */
    operator fun contains(property: Property<*>): Boolean {
        return backingMap.containsKey(property.key)
    }

    /**
     * Delegate value of property
     * @param prop Property to delegate value
     * @return ReadOnlyProperty
     */
    fun <T> delegate(prop: Property<T>): ReadOnlyProperty<Any, T?> = object : ReadOnlyProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return get(prop)
        }
    }

    /**
     * Delegate value of flag property
     * @param prop Property to delegate value
     * @return ReadOnlyProperty
     */
    fun delegate(prop: FlagProperty): ReadOnlyProperty<Any, Boolean> = object : ReadOnlyProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return get(prop)
        }
    }
}

/**
 * Simple mutable map which can store property-value pair
 */
open class MutablePropertyMap : PropertyMap() {
    /**
     * Set value of given property with vsalue
     * @param property Property to set
     * @param value Value to set
     */
    operator fun <T> set(property: MutableProperty<T>, value: T?) {
        if (value == null)
            backingMap.remove(property.key)
        else
            backingMap[property.key] = value
    }

    /**
     * Set value of given flag property with value
     * @param property Property to set
     * @param value Value to set
     */
    operator fun set(property: MutableFlagProperty, value: Boolean) {
        if (value)
            backingMap[property.key] = Unit
        else
            backingMap.remove(property.key)
    }

    /**
     * Delegate value of property
     * @param prop Property to delegate value
     * @return ReadWriteProperty
     */
    fun <T> mutableDelegate(prop: MutableProperty<T>): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return get(prop)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            set(prop, value)
        }
    }

    /**
     * Delegate value of flag property
     * @param prop Property to delegate value
     * @return ReadWriteProperty
     */
    fun mutableDelegate(prop: MutableFlagProperty): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return get(prop)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            set(prop, value)
        }
    }
}