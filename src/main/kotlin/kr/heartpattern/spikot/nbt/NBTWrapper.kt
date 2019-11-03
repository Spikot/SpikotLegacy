package kr.heartpattern.spikot.nbt

import java.util.*

interface WrapperNBTBase<T : Any> {
    val tag: Any
    val type: TagType<T>
    val value: T
}

interface WrapperNBTNumber<T : Number> : WrapperNBTBase<T> {
    val long: Long
        get() = value.toLong()
    val int: Int
        get() = value.toInt()
    val short: Short
        get() = value.toShort()
    val byte: Byte
        get() = value.toByte()
    val double: Double
        get() = value.toDouble()
    val float: Float
        get() = value.toFloat()
}


interface WrapperNBTEnd : WrapperNBTBase<Unit> {
    override val type: TagType<Unit>
        get() = TagType.END
}

interface WrapperNBTByte : WrapperNBTNumber<Byte> {
    override val type: TagType<Byte>
        get() = TagType.BYTE
}

interface WrapperNBTLong : WrapperNBTNumber<Long> {
    override val type: TagType<Long>
        get() = TagType.LONG
}

interface WrapperNBTInt : WrapperNBTNumber<Int> {
    override val type: TagType<Int>
        get() = TagType.INT
}

interface WrapperNBTShort : WrapperNBTNumber<Short> {
    override val type: TagType<Short>
        get() = TagType.SHORT
}

interface WrapperNBTDouble : WrapperNBTNumber<Double> {
    override val type: TagType<Double>
        get() = TagType.DOUBLE
}

interface WrapperNBTFloat : WrapperNBTNumber<Float> {
    override val type: TagType<Float>
        get() = TagType.FLOAT
}

interface WrapperNBTByteArray : WrapperNBTBase<ByteArray> {
    override val type: TagType<ByteArray>
        get() = TagType.BYTE_ARRAY
}

interface WrapperNBTIntArray : WrapperNBTBase<IntArray> {
    override val type: TagType<IntArray>
        get() = TagType.INT_ARRAY
}

interface WrapperNBTLongArray : WrapperNBTBase<LongArray> {
    override val type: TagType<LongArray>
        get() = TagType.LONG_ARRAY
}

interface WrapperNBTString : WrapperNBTBase<String> {
    override val type: TagType<String>
        get() = TagType.STRING
}

@Suppress("UNCHECKED_CAST")
interface WrapperNBTList<W : WrapperNBTBase<*>> : WrapperNBTBase<MutableList<W>>, MutableList<W> {
    override val type: TagType<MutableList<W>>
        get() = TagType.LIST as TagType<MutableList<W>>
    val enclosing: TagType<*>
}

fun <T : Any> WrapperNBTList<WrapperNBTBase<T>>.getValue(index: Int): T = get(index).value

@Suppress("UNCHECKED_CAST")
interface WrapperNBTCompound : WrapperNBTBase<MutableMap<String, WrapperNBTBase<*>>>, MutableMap<String, WrapperNBTBase<*>> {
    override val type: TagType<MutableMap<String, WrapperNBTBase<*>>>
        get() = TagType.COMPOUND as TagType<MutableMap<String, WrapperNBTBase<*>>>

    fun hasKeyOfType(key: String, type: TagType<*>): Boolean
    fun hasUUID(key: String): Boolean

    fun getNBTEnd(key: String): WrapperNBTEnd
    fun getNBTByte(key: String): WrapperNBTByte
    fun getNBTLong(key: String): WrapperNBTLong
    fun getNBTInt(key: String): WrapperNBTInt
    fun getNBTShort(key: String): WrapperNBTShort
    fun getNBTDouble(key: String): WrapperNBTDouble
    fun getNBTFloat(key: String): WrapperNBTFloat
    fun getNBTByteArray(key: String): WrapperNBTByteArray
    fun getNBTIntArray(key: String): WrapperNBTIntArray
    fun getNBTLongArray(key: String): WrapperNBTLongArray
    fun getNBTString(key: String): WrapperNBTString
    @Suppress("UNCHECKED_CAST")
    fun <W : WrapperNBTBase<*>> getNBTList(key: String): WrapperNBTList<W>

    fun getNBTCompound(key: String): WrapperNBTCompound

    fun setNBTEnd(key: String, value: WrapperNBTEnd)
    fun setNBTByte(key: String, value: WrapperNBTByte)
    fun setNBTLong(key: String, value: WrapperNBTLong)
    fun setNBTInt(key: String, value: WrapperNBTInt)
    fun setNBTShort(key: String, value: WrapperNBTShort)
    fun setNBTDouble(key: String, value: WrapperNBTDouble)
    fun setNBTFloat(key: String, value: WrapperNBTFloat)
    fun setNBTByteArray(key: String, value: WrapperNBTByteArray)
    fun setNBTIntArray(key: String, value: WrapperNBTIntArray)
    fun setNBTLongArray(key: String, value: WrapperNBTLongArray)
    fun setNBTString(key: String, value: WrapperNBTString)
    @Suppress("UNCHECKED_CAST")
    fun <W : WrapperNBTBase<*>> setNBTList(key: String, value: WrapperNBTList<W>)

    fun setNBTCompound(key: String, value: WrapperNBTCompound)

    fun getEnd(key: String)
    fun getBoolean(key: String): Boolean
    fun getByte(key: String): Byte
    fun getLong(key: String): Long
    fun getInt(key: String): Int
    fun getShort(key: String): Short
    fun getDouble(key: String): Double
    fun getFloat(key: String): Float
    fun getByteArray(key: String): ByteArray
    fun getIntArray(key: String): IntArray
    fun getLongArray(key: String): LongArray
    fun getString(key: String): String
    fun <W : WrapperNBTBase<*>> getList(key: String): MutableList<W>
    fun getCompound(key: String): MutableMap<String, WrapperNBTBase<*>>
    fun getUUID(key: String): UUID

    fun setEnd(key: String, value: Unit)
    fun setBoolean(key: String, value: Boolean)
    fun setByte(key: String, value: Byte)
    fun setLong(key: String, value: Long)
    fun setInt(key: String, value: Int)
    fun setShort(key: String, value: Short)
    fun setDouble(key: String, value: Double)
    fun setFloat(key: String, value: Float)
    fun setByteArray(key: String, value: ByteArray)
    fun setIntArray(key: String, value: IntArray)
    fun setLongArray(key: String, value: LongArray)
    fun setString(key: String, value: String)
    fun <W : WrapperNBTBase<*>> setList(key: String, value: List<W>)
    fun setCompound(key: String, value: MutableMap<String, WrapperNBTBase<*>>)
    fun setUUID(key: String, value: UUID)
}