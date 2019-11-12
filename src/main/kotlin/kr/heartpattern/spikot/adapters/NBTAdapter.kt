package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.nbt.*
import kr.heartpattern.spikot.nbt.TagType.*

interface NBTAdapter : IAdapter {
    fun wrapNBTEnd(tag: Any): WrapperNBTEnd
    fun wrapNBTByte(tag: Any): WrapperNBTByte
    fun wrapNBTLong(tag: Any): WrapperNBTLong
    fun wrapNBTInt(tag: Any): WrapperNBTInt
    fun wrapNBTShort(tag: Any): WrapperNBTShort
    fun wrapNBTDouble(tag: Any): WrapperNBTDouble
    fun wrapNBTFloat(tag: Any): WrapperNBTFloat
    fun wrapNBTByteArray(tag: Any): WrapperNBTByteArray
    fun wrapNBTIntArray(tag: Any): WrapperNBTIntArray
    fun wrapNBTLongArray(tag: Any): WrapperNBTLongArray
    fun wrapNBTString(tag: Any): WrapperNBTString
    fun <W : WrapperNBTBase<*>> wrapNBTList(tag: Any): WrapperNBTList<W>
    fun wrapNBTCompound(tag: Any): WrapperNBTCompound

    fun createNBTEnd(value: Unit): WrapperNBTEnd
    fun createNBTByte(value: Byte): WrapperNBTByte
    fun createNBTLong(value: Long): WrapperNBTLong
    fun createNBTInt(value: Int): WrapperNBTInt
    fun createNBTShort(value: Short): WrapperNBTShort
    fun createNBTDouble(value: Double): WrapperNBTDouble
    fun createNBTFloat(value: Float): WrapperNBTFloat
    fun createNBTByteArray(value: ByteArray): WrapperNBTByteArray
    fun createNBTIntArray(value: IntArray): WrapperNBTIntArray
    fun createNBTLongArray(value: LongArray): WrapperNBTLongArray
    fun createNBTString(value: String): WrapperNBTString
    fun <W : WrapperNBTBase<*>> createNBTList(value: List<W>): WrapperNBTList<W>
    fun createNBTCompound(value: Map<String, WrapperNBTBase<*>>): WrapperNBTCompound

    fun getType(tag: Any): TagType<*>

    fun compressNBT(nbt: WrapperNBTCompound): ByteArray
    fun decompressNBT(array: ByteArray): WrapperNBTCompound

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