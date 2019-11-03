package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.NBTAdapter
import kr.heartpattern.spikot.misc.Converter
import kr.heartpattern.spikot.misc.MutableConvertingList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class NBTAccessor(val tag: WrapperNBTCompound) {
    operator fun contains(key: String): Boolean {
        return tag.contains(key)
    }

    fun has(key: String): Boolean {
        return key in this
    }

    fun has(key: String, type: TagType<*>): Boolean {
        return tag.hasKeyOfType(key, type)
    }

    protected val boolean = TagDelegate(
        { key -> has(key, TagType.BYTE) },
        WrapperNBTCompound::getBoolean,
        WrapperNBTCompound::setBoolean
    )
    protected val byte = TagDelegate(
        { key -> has(key, TagType.BYTE) },
        WrapperNBTCompound::getByte,
        WrapperNBTCompound::setByte
    )
    protected val short = TagDelegate(
        { key -> has(key, TagType.SHORT) },
        WrapperNBTCompound::getShort,
        WrapperNBTCompound::setShort
    )
    protected val int = TagDelegate(
        { key -> has(key, TagType.INT) },
        WrapperNBTCompound::getInt,
        WrapperNBTCompound::setInt
    )
    protected val long = TagDelegate(
        { key -> has(key, TagType.LONG) },
        WrapperNBTCompound::getLong,
        WrapperNBTCompound::setLong
    )
    protected val float = TagDelegate(
        { key -> has(key, TagType.FLOAT) },
        WrapperNBTCompound::getFloat,
        WrapperNBTCompound::setFloat
    )
    protected val double = TagDelegate(
        { key -> has(key, TagType.DOUBLE) },
        WrapperNBTCompound::getDouble,
        WrapperNBTCompound::setDouble
    )
    protected val byteArray = TagDelegate(
        { key -> has(key, TagType.BYTE_ARRAY) },
        WrapperNBTCompound::getByteArray,
        WrapperNBTCompound::setByteArray
    )
    protected val string = TagDelegate(
        { key -> has(key, TagType.STRING) },
        WrapperNBTCompound::getString,
        WrapperNBTCompound::setString
    )
    protected val intArray = TagDelegate(
        { key -> has(key, TagType.INT_ARRAY) },
        WrapperNBTCompound::getIntArray,
        WrapperNBTCompound::setIntArray
    )
    protected val longArray = TagDelegate(
        { key -> has(key, TagType.LONG_ARRAY) },
        WrapperNBTCompound::getLongArray,
        WrapperNBTCompound::setLongArray
    )
    protected val uuid = TagDelegate(
        WrapperNBTCompound::hasUUID,
        WrapperNBTCompound::getUUID,
        WrapperNBTCompound::setUUID
    )

    protected inline fun <reified T : Enum<T>> enum() = enum(T::class)

    protected fun <T : Enum<T>> enum(enum: KClass<T>) =
        EnumWrappingDelegate(enum)

    protected fun <T : NBTAccessor> tag(constructor: (WrapperNBTCompound) -> T): CompoundDelegate<T> {
        return CompoundDelegate(constructor)
    }

    protected fun <T : Any> list(type: TagType<T>): ReadWriteProperty<NBTAccessor, MutableList<T>> {
        return object : ReadWriteProperty<NBTAccessor, MutableList<T>> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): MutableList<T> {
                val list = thisRef.tag.getNBTList<WrapperNBTBase<T>>(property.name)
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
                thisRef.tag.setList(property.name, value.map { NBTAdapter.createNBT(it) })
            }
        }
    }
}