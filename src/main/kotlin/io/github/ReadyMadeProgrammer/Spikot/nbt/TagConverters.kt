package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.*
import io.github.ReadyMadeProgrammer.Spikot.misc.Converter

interface TagConverter<T : Any> : Converter<NBTBase, T> {
    companion object {
        val BOOLEAN =
            create<Boolean>(
                TagType.BYTE,
                { NBTTagByte(if (it) 1 else 0) }) { (it as NBTTagByte).g() == 1.toByte() }
        val BYTE = create(
            TagType.BYTE,
            ::NBTTagByte
        ) { (it as NBTTagByte).g() }
        val SHORT = create(
            TagType.SHORT,
            ::NBTTagShort
        ) { (it as NBTTagShort).f() }
        val INT = create(
            TagType.INT,
            ::NBTTagInt
        ) { (it as NBTTagInt).e() }
        val LONG = create(
            TagType.LONG,
            ::NBTTagLong
        ) { (it as NBTTagLong).d() }
        val FLOAT = create(
            TagType.FLOAT,
            ::NBTTagFloat
        ) { (it as NBTTagFloat).i() }
        val DOUBLE = create(
            TagType.DOUBLE,
            ::NBTTagDouble
        ) { (it as NBTTagDouble).asDouble() }
        val BYTE_ARRAY = create(
            TagType.BYTE_ARRAY,
            ::NBTTagByteArray
        ) { (it as NBTTagByteArray).c() }
        val STRING = create(
            TagType.STRING,
            ::NBTTagString
        ) { (it as NBTTagString).c_() }
        val INT_ARRAY = create(
            TagType.INT_ARRAY,
            ::NBTTagIntArray
        ) { (it as NBTTagIntArray).d() }
        val LONG_ARRAY = create(
            TagType.LONG_ARRAY,
            ::NBTTagLongArray
        ) { (it as NBTTagLongArray).get() }

        fun <T : NBTAccessor> TAG(constructor: (NBTTagCompound) -> T): TagConverter<T> {
            return create(
                TagType.TAG,
                { it.nbtTagCompound }) { constructor(it as NBTTagCompound) }
        }
    }

    val target: TagType
}

private fun <T : Any> create(
    type: TagType,
    toTagLambda: (T) -> NBTBase,
    fromTagLambda: (NBTBase) -> T
): SimpleTagConverter<T> {
    return SimpleTagConverter(type, fromTagLambda, toTagLambda)
}

class SimpleTagConverter<T : Any>(
    override val target: TagType,
    private val fromTagLambda: (NBTBase) -> T,
    private val toTagLambda: (T) -> NBTBase
) : TagConverter<T> {
    override fun read(raw: NBTBase): T {
        return fromTagLambda(raw)
    }

    override fun write(converted: T): NBTBase {
        return toTagLambda(converted)
    }
}

fun <T : Any, R : Any> TagConverter<T>.convert(converter: Converter<T, R>): TagConverter<R> {
    return create(target, { write(converter.write(it)) }, { converter.read(read(it)) })
}