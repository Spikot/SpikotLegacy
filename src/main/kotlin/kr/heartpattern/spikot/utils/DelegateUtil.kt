package kr.heartpattern.spikot.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <R, T> ReadOnlyProperty<R, T?>.nonnull(): ReadOnlyProperty<R, T> {
    return object : ReadOnlyProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }
    }
}

fun <R, T> ReadWriteProperty<R, T?>.nonnull(): ReadWriteProperty<R, T> {
    return object : ReadWriteProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
            return this@nonnull.setValue(thisRef, property, value)
        }
    }
}