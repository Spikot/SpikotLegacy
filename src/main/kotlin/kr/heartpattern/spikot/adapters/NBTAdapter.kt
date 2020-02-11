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

package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.nbt.*
import kr.heartpattern.spikot.nbt.TagType.*

/**
 * Adapter to handle nms NBT tag
 */
interface NBTAdapter : IAdapter {

    /**
     * Wrap nms.NBTTagEnd
     * @param tag nms.NBTTagEnd
     * @return Wrapped Tag
     */
    fun wrapNBTEnd(tag: Any): WrapperNBTEnd

    /**
     * Wrap nms.NBTTagByte
     * @param tag nms.NBTTagByte
     * @return Wrapped Tag
     */
    fun wrapNBTByte(tag: Any): WrapperNBTByte

    /**
     * Wrap nms.NBTTagLong
     * @param tag nms.NBTTagLong
     * @return Wrapped Tag
     */
    fun wrapNBTLong(tag: Any): WrapperNBTLong

    /**
     * Wrap nms.NBTTagInt
     * @param tag nms.NBTTagInt
     * @return Wrapped Tag
     */
    fun wrapNBTInt(tag: Any): WrapperNBTInt

    /**
     * Wrap nms.NBTTagShort
     * @param tag nms.NBTTagShort
     * @return Wrapped Tag
     */
    fun wrapNBTShort(tag: Any): WrapperNBTShort

    /**
     * Wrap nms.NBTTagDouble
     * @param tag nms.NBTTagDouble
     * @return Wrapped Tag
     */
    fun wrapNBTDouble(tag: Any): WrapperNBTDouble

    /**
     * Wrap nms.NBTTagFloat
     * @param tag nms.NBTTagFloat
     * @return Wrapped Tag
     */
    fun wrapNBTFloat(tag: Any): WrapperNBTFloat

    /**
     * Wrap nms.NBTTagByteArray
     * @param tag nms.NBTTagByteArray
     * @return Wrapped Tag
     */
    fun wrapNBTByteArray(tag: Any): WrapperNBTByteArray

    /**
     * Wrap nms.NBTTagIntArray
     * @param tag nms.NBTTagIntArray
     * @return Wrapped Tag
     */
    fun wrapNBTIntArray(tag: Any): WrapperNBTIntArray

    /**
     * Wrap nms.NBTTagLongArray
     * @param tag nms.NBTTagLongArray
     * @return Wrapped Tag
     */
    fun wrapNBTLongArray(tag: Any): WrapperNBTLongArray

    /**
     * Wrap nms.NBTTagString
     * @param tag nms.NBTTagString
     * @return Wrapped Tag
     */
    fun wrapNBTString(tag: Any): WrapperNBTString

    /**
     * Wrap nms.NBTTagList
     * @param tag nms.NBTTagList
     * @return Wrapped Tag
     */
    fun <W : WrapperNBTBase<*>> wrapNBTList(tag: Any): WrapperNBTList<W>

    /**
     * Wrap nms.NBTTagCompound
     * @param tag nms.NBTTagCompound
     * @return Wrapped Tag
     */
    fun wrapNBTCompound(tag: Any): WrapperNBTCompound

    /**
     * Create new NBTTagEnd from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTEnd(value: Unit): WrapperNBTEnd

    /**
     * Create new NBTTagByte from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTByte(value: Byte): WrapperNBTByte

    /**
     * Create new NBTTagLong from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTLong(value: Long): WrapperNBTLong

    /**
     * Create new NBTTagInt from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTInt(value: Int): WrapperNBTInt

    /**
     * Create new NBTTagShort from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTShort(value: Short): WrapperNBTShort

    /**
     * Create new NBTTagDouble from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTDouble(value: Double): WrapperNBTDouble

    /**
     * Create new NBTTagFloat from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTFloat(value: Float): WrapperNBTFloat

    /**
     * Create new NBTTagByteArray from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTByteArray(value: ByteArray): WrapperNBTByteArray

    /**
     * Create new NBTTagIntArray from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTIntArray(value: IntArray): WrapperNBTIntArray

    /**
     * Create new NBTTagLongArray from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTLongArray(value: LongArray): WrapperNBTLongArray

    /**
     * Create new NBTTagString from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTString(value: String): WrapperNBTString

    /**
     * Create new NBTTagList from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun <W : WrapperNBTBase<*>> createNBTList(value: List<W>): WrapperNBTList<W>

    /**
     * Create new NBTTagCompound from given value
     * @param value Value for tag
     * @return Wrapped tag
     */
    fun createNBTCompound(value: Map<String, WrapperNBTBase<*>>): WrapperNBTCompound

    /**
     * Get type of given nms tag
     * @param tag nms tag
     * @return Tag type
     */
    fun getType(tag: Any): TagType<*>

    /**
     * Compress given nbt to byte array
     * @param nbt Wrapped nbt tag to compress
     * @return Compressed byte array
     */
    fun compressNBT(nbt: WrapperNBTCompound): ByteArray

    /**
     * Decompress given byte array to nbt
     * @param array Compressed byte array
     * @return Decompressed nbt tag
     */
    fun decompressNBT(array: ByteArray): WrapperNBTCompound

    /**
     * Wrap any type of nms nbt
     * @param tag Any nms nbt
     * @return Wrapped nbt with corresponding type
     */
    fun wrapNBT(tag: Any): WrapperNBTBase<*> {
        return when (getType(tag)) {
            END -> wrapNBTEnd(tag)
            BYTE -> wrapNBTByte(tag)
            SHORT -> wrapNBTShort(tag)
            INT -> wrapNBTInt(tag)
            LONG -> wrapNBTLong(tag)
            FLOAT -> wrapNBTFloat(tag)
            DOUBLE -> wrapNBTDouble(tag)
            BYTE_ARRAY -> wrapNBTByteArray(tag)
            STRING -> wrapNBTString(tag)
            LIST -> wrapNBTList<WrapperNBTBase<*>>(tag)
            COMPOUND -> wrapNBTCompound(tag)
            INT_ARRAY -> wrapNBTIntArray(tag)
            LONG_ARRAY -> wrapNBTLongArray(tag)
            MISC -> wrapNBTLong(tag)
        }
    }

    /**
     * Create nbt from given any value
     * @param value any value
     * @return Wrapped created tag
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> createNBT(value: T): WrapperNBTBase<T> {
        return when (value) {
            is Unit -> createNBTEnd(value)
            is Byte -> createNBTByte(value)
            is Short -> createNBTShort(value)
            is Int -> createNBTInt(value)
            is Long -> createNBTLong(value)
            is Float -> createNBTFloat(value)
            is Double -> createNBTDouble(value)
            is ByteArray -> createNBTByteArray(value)
            is String -> createNBTString(value)
            is List<*> -> createNBTList(value as List<WrapperNBTBase<*>>)
            is Map<*, *> -> createNBTCompound(value as Map<String, WrapperNBTBase<*>>)
            is IntArray -> createNBTIntArray(value)
            is LongArray -> createNBTLongArray(value)
            else -> throw IllegalArgumentException("Unsupported nbt type")
        } as WrapperNBTBase<T>
    }

    @Module
    object Resolver : VersionAdapterResolver<NBTAdapter>(NBTAdapter::class, VersionType.BUKKIT)

    companion object : NBTAdapter by Resolver.default
}