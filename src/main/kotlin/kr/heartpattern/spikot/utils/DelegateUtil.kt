package kr.heartpattern.spikot.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Convert nullable property delegate to nonnull property delegate
 * @receiver nullable property delegate
 * @return nonnull property delegate
 */
fun <R, T> ReadOnlyProperty<R, T?>.nonnull(): ReadOnlyProperty<R, T> {
    return object : ReadOnlyProperty<R, T> {
        override fun getValue(thisRef: R, property: KProperty<*>): T {
            return this@nonnull.getValue(thisRef, property)!!
        }
    }
}

/**
 * Convert nullable property delegate to nonnull property delegate
 * @receiver nullable property delegate
 * @return nonnull property delegate
 */
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