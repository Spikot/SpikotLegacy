package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.NBTTagCompound
import io.github.ReadyMadeProgrammer.Spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TagDelegate<T>(
    private val check: NBTTagCompound.(String) -> Boolean,
    private val getter: NBTTagCompound.(String) -> T,
    private val setter: NBTTagCompound.(String, T) -> Unit
) : ReadWriteProperty<NBTAccessor, T> {
    constructor(type: TagType, getter: NBTTagCompound.(String) -> T, setter: NBTTagCompound.(String, T) -> Unit) : this(
        { hasKeyOfType(it, type.id) },
        getter,
        setter
    )

    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
        val value = thisRef.nbtTagCompound.getter(property.name)
        if (!thisRef.nbtTagCompound.check(property.name)) {
            thisRef.nbtTagCompound.setter(property.name, value)
        }
        return value
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setter(property.name, value)
    }

    fun <R> convert(converter: Converter<T, R>): TagDelegate<R> = TagDelegate(
        check,
        { converter.read(getter(it)) },
        { name, value -> setter(name, converter.write(value)) }
    )
}

class CompoundDelegate<T : NBTAccessor>(private val constructor: (NBTTagCompound) -> T) :
    ReadWriteProperty<NBTAccessor, T> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
        val value = thisRef.nbtTagCompound.getCompound(property.name)
        if (!thisRef.nbtTagCompound.hasKeyOfType(property.name, TagType.TAG.id)) {
            thisRef.nbtTagCompound.setCompound(property.name, value)
        }
        return constructor(value)
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setCompound(property.name, value.nbtTagCompound)
    }
}

class EnumWrappingDelegate<T : Enum<T>>(
    enumClass: KClass<T>
) : ReadWriteProperty<NBTAccessor, T> {
    private val constants: Array<T> = enumClass.java.enumConstants
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
        return constants[thisRef.nbtTagCompound.getInt(property.name)]
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setInt(property.name, value.ordinal)
    }
}