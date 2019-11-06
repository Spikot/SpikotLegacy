package kr.heartpattern.spikot.misc

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface Property<T> {
    val key: String
        get() = this::class.qualifiedName!!
}

interface MutableProperty<T> : Property<T>

abstract class AbstractMutableProperty<T>(private val property: Property<T>) : MutableProperty<T> {
    override val key: String
        get() = property.key
}

interface FlagProperty : Property<Unit>

interface MutableFlagProperty : MutableProperty<Unit>, FlagProperty

@Suppress("UNCHECKED_CAST")
open class PropertyMap {
    protected val backingMap = HashMap<String, Any?>()

    operator fun <T> get(property: Property<T>): T? {
        return backingMap[property.key] as T?
    }

    operator fun get(property: FlagProperty): Boolean {
        return backingMap.contains(property.key)
    }

    operator fun contains(property: Property<*>): Boolean {
        return backingMap.containsKey(property.key)
    }

    fun <T> delegate(prop: Property<T>): ReadOnlyProperty<Any, T?> = object : ReadOnlyProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return get(prop)
        }
    }

    fun delegate(prop: FlagProperty): ReadOnlyProperty<Any, Boolean> = object : ReadOnlyProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return get(prop)
        }
    }
}

open class MutablePropertyMap : PropertyMap() {
    operator fun <T> set(property: MutableProperty<T>, value: T?) {
        if (value == null)
            backingMap.remove(property.key)
        else
            backingMap[property.key] = value
    }

    operator fun set(property: MutableFlagProperty, value: Boolean) {
        if (value)
            backingMap[property.key] = Unit
        else
            backingMap.remove(property.key)
    }

    fun <T> mutableDelegate(prop: MutableProperty<T>): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return get(prop)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            set(prop, value)
        }
    }

    fun mutableDelegate(prop: MutableFlagProperty): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return get(prop)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            set(prop, value)
        }
    }
}