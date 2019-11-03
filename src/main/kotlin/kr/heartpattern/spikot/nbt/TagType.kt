package kr.heartpattern.spikot.nbt

import kotlin.reflect.KClass

sealed class TagType<T : Any>(val id: Int, val type: KClass<T>) {
    object END : TagType<Unit>(0, Unit::class)
    object BYTE : TagType<Byte>(1, Byte::class)
    object SHORT : TagType<Short>(2, Short::class)
    object INT : TagType<Int>(3, Int::class)
    object LONG : TagType<Long>(4, Long::class)
    object FLOAT : TagType<Float>(5, Float::class)
    object DOUBLE : TagType<Double>(6, Double::class)
    object BYTE_ARRAY : TagType<ByteArray>(7, ByteArray::class)
    object STRING : TagType<String>(8, String::class)
    object LIST : TagType<MutableList<*>>(9, MutableList::class)
    object COMPOUND : TagType<MutableMap<*, *>>(10, MutableMap::class)
    object INT_ARRAY : TagType<IntArray>(11, IntArray::class)
    object LONG_ARRAY : TagType<LongArray>(12, LongArray::class)
    object MISC : TagType<Any>(99, Any::class)
}