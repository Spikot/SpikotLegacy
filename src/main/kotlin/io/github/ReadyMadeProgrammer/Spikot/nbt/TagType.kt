package io.github.ReadyMadeProgrammer.Spikot.nbt

import kotlin.reflect.KClass

enum class TagType(val id: Int, val type: KClass<*>) {
    BYTE(1, Byte::class),
    SHORT(2, Short::class),
    INT(3, Int::class),
    LONG(4, Long::class),
    FLOAT(5, Float::class),
    DOUBLE(6, Double::class),
    BYTE_ARRAY(7, ByteArray::class),
    STRING(8, String::class),
    LIST(9, MutableList::class),
    TAG(10, NBTAccessor::class),
    INT_ARRAY(11, IntArray::class),
    LONG_ARRAY(12, LongArray::class)
}