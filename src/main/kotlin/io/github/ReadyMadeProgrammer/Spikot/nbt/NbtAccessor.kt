package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.NBTBase
import io.github.ReadyMadeProgrammer.Spikot.NBTTagCompound
import io.github.ReadyMadeProgrammer.Spikot.NBTTagList
import io.github.ReadyMadeProgrammer.Spikot.misc.MutableConvertingList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class NbtAccessor {
    @PublishedApi
    internal lateinit var nbtTagCompound: NBTTagCompound

    operator fun contains(key: String): Boolean {
        return nbtTagCompound.hasKey(key)
    }

    fun has(key: String): Boolean {
        return key in this
    }

    fun has(key: String, type: TagType): Boolean {
        return nbtTagCompound.hasKeyOfType(key, type.id)
    }

    protected fun initialize(tag: NBTTagCompound) {
        nbtTagCompound = tag
    }

    protected val boolean = TagDelegate(
            TagType.BYTE,
            NBTTagCompound::getBoolean,
            NBTTagCompound::setBoolean
    )
    protected val byte = TagDelegate(
            TagType.BYTE,
            NBTTagCompound::getByte,
            NBTTagCompound::setByte
    )
    protected val short = TagDelegate(
            TagType.SHORT,
            NBTTagCompound::getShort,
            NBTTagCompound::setShort
    )
    protected val int = TagDelegate(
            TagType.INT,
            NBTTagCompound::getInt,
            NBTTagCompound::setInt
    )
    protected val long = TagDelegate(
            TagType.LONG,
            NBTTagCompound::getLong,
            NBTTagCompound::setLong
    )
    protected val float = TagDelegate(
            TagType.FLOAT,
            NBTTagCompound::getFloat,
            NBTTagCompound::setFloat
    )
    protected val double = TagDelegate(
            TagType.DOUBLE,
            NBTTagCompound::getDouble,
            NBTTagCompound::setDouble
    )
    protected val byteArray =
            TagDelegate(
                    TagType.BYTE_ARRAY,
                    NBTTagCompound::getByteArray,
                    NBTTagCompound::setByteArray
            )
    protected val string = TagDelegate(
            TagType.STRING,
            NBTTagCompound::getString,
            NBTTagCompound::setString
    )
    protected val intArray = TagDelegate(
            TagType.INT_ARRAY,
            NBTTagCompound::getIntArray,
            NBTTagCompound::setIntArray
    )
    protected val longArray =
            TagDelegate(
                    TagType.LONG_ARRAY,
                    NBTTagCompound::getLongArray,
                    NBTTagCompound::setLongArray
            )
    protected val uuid = TagDelegate(
            { hasUUID(it) },
            NBTTagCompound::getUUID,
            NBTTagCompound::setUUID
    )

    protected inline fun <reified T : Enum<T>> enum() =
            enum(T::class)

    protected fun <T : Enum<T>> enum(enum: KClass<T>) =
            EnumWrappingDelegate(enum)

    protected fun <T : NbtAccessor> tag(constructor: (NBTTagCompound) -> T): CompoundDelegate<T> {
        return CompoundDelegate(constructor)
    }

    protected fun <T : Any> list(converter: TagConverter<T>): ReadWriteProperty<NbtAccessor, MutableList<T>> {
        return object : ReadWriteProperty<NbtAccessor, MutableList<T>> {
            override fun getValue(thisRef: NbtAccessor, property: KProperty<*>): MutableList<T> {
                val list = thisRef.nbtTagCompound.getList(property.name, converter.target.id)
                if (!thisRef.nbtTagCompound.hasKeyOfType(property.name, TagType.LIST.id)) {
                    thisRef.nbtTagCompound.set(property.name, list)
                }
                return MutableConvertingList(
                        list.list,
                        converter
                )
            }

            override fun setValue(thisRef: NbtAccessor, property: KProperty<*>, value: MutableList<T>) {
                val list = NBTTagList()
                if (value is NBTTagConvertingList<*>) {
                    list.list = value.backingList
                } else {
                    list.list = value.map(converter::write)
                }
                thisRef.nbtTagCompound.set(property.name, list)
            }
        }
    }

    private class NBTTagConvertingList<T : Any>(list: MutableList<NBTBase>, converter: TagConverter<T>) :
            MutableConvertingList<NBTBase, T>(
                    list,
                    converter
            )
}