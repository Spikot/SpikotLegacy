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

import kr.heartpattern.spikot.adapters.NBTAdapter
import kr.heartpattern.spikot.misc.Converter
import kr.heartpattern.spikot.misc.MutableConvertingList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * NBTCompound structure declaration and accessor.
 */
abstract class NBTAccessor(val tag: WrapperNBTCompound) {
    /**
     * Check this tag contains key
     * @param key Name of key
     * @return Whether key is in tag
     */
    operator fun contains(key: String): Boolean {
        return tag.contains(key)
    }

    /**
     * Check this tag contains key
     * @param key Name of key
     * @return Whether key is in tag
     */
    fun has(key: String): Boolean {
        return key in this
    }

    /**
     * Check this tag contains key with type
     * @param key Name of key
     * @param type Type of key
     * @return Whether key is in tag and match with tag
     */
    fun has(key: String, type: TagType<*>): Boolean {
        return tag.hasKeyOfType(key, type)
    }

    /**
     * Delegate boolean tag
     */
    protected val boolean = TagDelegate(
        { key -> has(key, TagType.BYTE) },
        WrapperNBTCompound::getBoolean,
        WrapperNBTCompound::setBoolean
    )

    /**
     * Delegate byte tag
     */
    protected val byte = TagDelegate(
        { key -> has(key, TagType.BYTE) },
        WrapperNBTCompound::getByte,
        WrapperNBTCompound::setByte
    )

    /**
     * Delegate short tag
     */
    protected val short = TagDelegate(
        { key -> has(key, TagType.SHORT) },
        WrapperNBTCompound::getShort,
        WrapperNBTCompound::setShort
    )

    /**
     * Delegate int tag
     */
    protected val int = TagDelegate(
        { key -> has(key, TagType.INT) },
        WrapperNBTCompound::getInt,
        WrapperNBTCompound::setInt
    )

    /**
     * Delegate long tag
     */
    protected val long = TagDelegate(
        { key -> has(key, TagType.LONG) },
        WrapperNBTCompound::getLong,
        WrapperNBTCompound::setLong
    )

    /**
     * Delegate float tag
     */
    protected val float = TagDelegate(
        { key -> has(key, TagType.FLOAT) },
        WrapperNBTCompound::getFloat,
        WrapperNBTCompound::setFloat
    )

    /**
     * Delegate double tag
     */
    protected val double = TagDelegate(
        { key -> has(key, TagType.DOUBLE) },
        WrapperNBTCompound::getDouble,
        WrapperNBTCompound::setDouble
    )

    /**
     * Delegate byte array tag
     */
    protected val byteArray = TagDelegate(
        { key -> has(key, TagType.BYTE_ARRAY) },
        WrapperNBTCompound::getByteArray,
        WrapperNBTCompound::setByteArray
    )

    /**
     * Delegate string tag
     */
    protected val string = TagDelegate(
        { key -> has(key, TagType.STRING) },
        WrapperNBTCompound::getString,
        WrapperNBTCompound::setString
    )

    /**
     * Delegate int array tag
     */
    protected val intArray = TagDelegate(
        { key -> has(key, TagType.INT_ARRAY) },
        WrapperNBTCompound::getIntArray,
        WrapperNBTCompound::setIntArray
    )

    /**
     * Delegate long array tag
     */
    protected val longArray = TagDelegate(
        { key -> has(key, TagType.LONG_ARRAY) },
        WrapperNBTCompound::getLongArray,
        WrapperNBTCompound::setLongArray
    )

    /**
     * Delegate uuid tag
     */
    protected val uuid = TagDelegate(
        WrapperNBTCompound::hasUUID,
        WrapperNBTCompound::getUUID,
        WrapperNBTCompound::setUUID
    )

    /**
     * Delegte enum
     * @param T Enum type
     */
    protected inline fun <reified T : Enum<T>> enum() = enum(T::class)

    /**
     * Delegate Enum type
     * @param T Enum type
     * @param enum Enum class
     */
    protected fun <T : Enum<T>> enum(enum: KClass<T>) =
        EnumWrappingDelegate(enum)

    /**
     * Delegate nbt tag
     * @param constructor Constructor of nbt tag
     */
    protected fun <T : NBTAccessor> tag(constructor: (WrapperNBTCompound) -> T): CompoundDelegate<T> {
        return CompoundDelegate(constructor)
    }

    /**
     * Delegate nbt list
     * @param type Type of enclosing type
     */
    protected fun <T : Any> list(type: TagType<T>): ReadWriteProperty<NBTAccessor, MutableList<T>> {
        return object : ReadWriteProperty<NBTAccessor, MutableList<T>> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): MutableList<T> {
                val list = thisRef.tag.getList<WrapperNBTBase<T>>(property.name, type)
                if (list.enclosing != type)
                    throw ClassCastException("List type is ${list.enclosing}")
                return MutableConvertingList(
                    list,
                    object : Converter<WrapperNBTBase<T>, T> {
                        override fun read(raw: WrapperNBTBase<T>): T {
                            return raw.value
                        }

                        override fun write(converted: T): WrapperNBTBase<T> {
                            return NBTAdapter.createNBT(converted)
                        }
                    }
                )
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: MutableList<T>) {
                thisRef.tag.setList(property.name, NBTAdapter.createNBTList(value.map { NBTAdapter.createNBT(it) }))
            }
        }
    }
}