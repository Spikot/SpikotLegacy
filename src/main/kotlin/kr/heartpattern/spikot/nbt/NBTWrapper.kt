package kr.heartpattern.spikot.nbt

import java.util.*

/**
 * Base class for all nbt wrapper
 * @param T Java type representation
 */
interface WrapperNBTBase<T : Any> {
    /**
     * Raw NMS Tag
     */
    val tag: Any

    /**
     * Tag type
     */
    val type: TagType<T>

    /**
     * Java value of tag
     */
    val value: T
}

/**
 * Base class for all numeric nbt wrapper
 */
interface WrapperNBTNumber<T : Number> : WrapperNBTBase<T> {
    /**
     * Get in long
     */
    val long: Long
        get() = value.toLong()

    /**
     * Get in int
     */
    val int: Int
        get() = value.toInt()

    /**
     * Get in short
     */
    val short: Short
        get() = value.toShort()

    /**
     * Get in byte
     */
    val byte: Byte
        get() = value.toByte()

    /**
     * Get in double
     */
    val double: Double
        get() = value.toDouble()

    /**
     * Get in float
     */
    val float: Float
        get() = value.toFloat()
}

/**
 * Wrapper of NBTTagEnd
 */
interface WrapperNBTEnd : WrapperNBTBase<Unit> {
    override val type: TagType<Unit>
        get() = TagType.END
}

/**
 * Wrapper of NBTTagByte
 */
interface WrapperNBTByte : WrapperNBTNumber<Byte> {
    override val type: TagType<Byte>
        get() = TagType.BYTE
}

/**
 * Wrapper of NBTTagLong
 */
interface WrapperNBTLong : WrapperNBTNumber<Long> {
    override val type: TagType<Long>
        get() = TagType.LONG
}

/**
 * Wrapper of NBTTagInt
 */
interface WrapperNBTInt : WrapperNBTNumber<Int> {
    override val type: TagType<Int>
        get() = TagType.INT
}

/**
 * Wrapper of NBTTagShort
 */
interface WrapperNBTShort : WrapperNBTNumber<Short> {
    override val type: TagType<Short>
        get() = TagType.SHORT
}

/**
 * Wrapper of NBTTagDouble
 */
interface WrapperNBTDouble : WrapperNBTNumber<Double> {
    override val type: TagType<Double>
        get() = TagType.DOUBLE
}

/**
 * Wrapper of NBTTagFloat
 */
interface WrapperNBTFloat : WrapperNBTNumber<Float> {
    override val type: TagType<Float>
        get() = TagType.FLOAT
}

/**
 * Wrapper of NBTTagByteArray
 */
interface WrapperNBTByteArray : WrapperNBTBase<ByteArray> {
    override val type: TagType<ByteArray>
        get() = TagType.BYTE_ARRAY
}

/**
 * Wrapper of NBTTagIntArray
 */
interface WrapperNBTIntArray : WrapperNBTBase<IntArray> {
    override val type: TagType<IntArray>
        get() = TagType.INT_ARRAY
}

/**
 * Wrapper of NBTTagLongArray
 */
interface WrapperNBTLongArray : WrapperNBTBase<LongArray> {
    override val type: TagType<LongArray>
        get() = TagType.LONG_ARRAY
}

/**
 * Wrapper of NBTTagString
 */
interface WrapperNBTString : WrapperNBTBase<String> {
    override val type: TagType<String>
        get() = TagType.STRING
}

/**
 * Wrapper of NBTTagList
 */
@Suppress("UNCHECKED_CAST")
interface WrapperNBTList<W : WrapperNBTBase<*>> : WrapperNBTBase<MutableList<W>>, MutableList<W> {
    override val type: TagType<MutableList<W>>
        get() = TagType.LIST as TagType<MutableList<W>>
    val enclosing: TagType<*>
}

/**
 * Get value from NBTTagList
 * @param T Enclosing value type
 * @param index Index
 * @return Enclosing value in given index
 */
fun <T : Any> WrapperNBTList<WrapperNBTBase<T>>.getValue(index: Int): T = get(index).value

/**
 * Wrapper of NBTTagCompound
 */
@Suppress("UNCHECKED_CAST")
interface WrapperNBTCompound : WrapperNBTBase<MutableMap<String, WrapperNBTBase<*>>>, MutableMap<String, WrapperNBTBase<*>> {
    override val type: TagType<MutableMap<String, WrapperNBTBase<*>>>
        get() = TagType.COMPOUND as TagType<MutableMap<String, WrapperNBTBase<*>>>

    /**
     * Check if tag has key with type
     * @param key Name of key
     * @param type Type of value
     * @return Whether tag has key and value is given type
     */
    fun hasKeyOfType(key: String, type: TagType<*>): Boolean

    /**
     * Check if tag has uuid
     * @param key Name of key
     * @return Whether tag has key of uuid
     */
    fun hasUUID(key: String): Boolean

    /**
     * Get enclosing value of NBTTagEnd
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getEnd(key: String): Unit

    /**
     * Get enclosing value of NBTTagBoolean
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getBoolean(key: String): Boolean

    /**
     * Get enclosing value of NBTTagByte
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getByte(key: String): Byte

    /**
     * Get enclosing value of NBTTagLong
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getLong(key: String): Long

    /**
     * Get enclosing value of NBTTagInt
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getInt(key: String): Int

    /**
     * Get enclosing value of NBTTagShort
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getShort(key: String): Short

    /**
     * Get enclosing value of NBTTagDouble
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getDouble(key: String): Double

    /**
     * Get enclosing value of NBTTagFloat
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getFloat(key: String): Float

    /**
     * Get enclosing value of NBTTagByteArray
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getByteArray(key: String): ByteArray

    /**
     * Get enclosing value of NBTTagIntArray
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getIntArray(key: String): IntArray

    /**
     * Get enclosing value of NBTTagLongArray
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getLongArray(key: String): LongArray

    /**
     * Get enclosing value of NBTTagString
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getString(key: String): String

    /**
     * Get enclosing value of NBTTagList
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun <W : WrapperNBTBase<*>> getList(key: String, type: TagType<*>): WrapperNBTList<W>

    /**
     * Get enclosing value of NBTTagCompound
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getCompound(key: String): WrapperNBTCompound

    /**
     * Get enclosing value of UUID
     * @param key NBTCompound key
     * @return Enclosing value of given key
     */
    fun getUUID(key: String): UUID

    /**
     * Set enclosing value of NBTTagEnd
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setEnd(key: String, value: Unit)

    /**
     * Set enclosing value of NBTTagBoolean
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setBoolean(key: String, value: Boolean)

    /**
     * Set enclosing value of NBTTagByte
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setByte(key: String, value: Byte)

    /**
     * Set enclosing value of NBTTagLong
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setLong(key: String, value: Long)

    /**
     * Set enclosing value of NBTTagInt
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setInt(key: String, value: Int)

    /**
     * Set enclosing value of NBTTagShort
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setShort(key: String, value: Short)

    /**
     * Set enclosing value of NBTTagDouble
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setDouble(key: String, value: Double)

    /**
     * Set enclosing value of NBTTagFloat
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setFloat(key: String, value: Float)

    /**
     * Set enclosing value of NBTTagByteArray
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setByteArray(key: String, value: ByteArray)

    /**
     * Set enclosing value of NBTTagIntArray
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setIntArray(key: String, value: IntArray)

    /**
     * Set enclosing value of NBTTagLongArray
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setLongArray(key: String, value: LongArray)

    /**
     * Set enclosing value of NBTTagString
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setString(key: String, value: String)

    /**
     * Set enclosing value of NBTTagList
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun <W : WrapperNBTBase<*>> setList(key: String, value: WrapperNBTList<W>)

    /**
     * Set enclosing value of NBTTagCompound
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setCompound(key: String, value: WrapperNBTCompound)

    /**
     * Set enclosing value of UUID
     * @param key NBTCompound key
     * @param value Value to set enclosing value
     */
    fun setUUID(key: String, value: UUID)
}