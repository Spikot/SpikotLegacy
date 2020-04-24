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

package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Delegate nbt tag
 * @param check Check if tag is applicable for this delegate
 * @param getter Get tag from nbt
 * @param setter Set tag to nbt
 */
class TagDelegate<T : Any>(
    val check: WrapperNBTCompound.(String) -> Boolean,
    val getter: WrapperNBTCompound.(String) -> T,
    val setter: WrapperNBTCompound.(String, T) -> Unit
) : ReadWriteProperty<NBTAccessor, T?> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (thisRef.tag.check(property.name)) {
            return thisRef.tag.getter(property.name)
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag.setter(property.name, value)
        }
    }

    /**
     * Get converted tag
     * @param converter Type converter
     */
    fun <R : Any> convert(converter: Converter<T, R>): TagDelegate<R> = TagDelegate(
        check,
        { converter.read(getter(it)) },
        { name, value -> setter(name, converter.write(value)) }
    )

    /**
     * Get not null delegate
     */
    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@TagDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@TagDelegate.setValue(thisRef, property, value)
            }
        }
}

/**
 * Delegate compound tag
 * @param constructor NBTAccessor constructor
 */
class CompoundDelegate<T : NBTAccessor>(private val constructor: (WrapperNBTCompound) -> T) :
    ReadWriteProperty<NBTAccessor, T?> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (!thisRef.tag.hasKeyOfType(property.name, TagType.COMPOUND)) {
            return constructor(thisRef.tag.getCompound(property.name))
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag[property.name] = value.tag
        }
    }

    /**
     * Get not null delegate
     */
    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@CompoundDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@CompoundDelegate.setValue(thisRef, property, value)
            }
        }
}

/**
 * Delegate enum class
 * @param enumClass Enum class
 */
class EnumWrappingDelegate<T : Enum<T>>(
    enumClass: KClass<T>
) : ReadWriteProperty<NBTAccessor, T?> {
    private val constants: Array<T> = enumClass.java.enumConstants
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (thisRef.tag.hasKeyOfType(property.name, TagType.INT)) {
            return constants[thisRef.tag.getInt(property.name)]
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag.setInt(property.name, value.ordinal)
        }
    }

    /**
     * Get not null delegate
     */
    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@EnumWrappingDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@EnumWrappingDelegate.setValue(thisRef, property, value)
            }
        }
}