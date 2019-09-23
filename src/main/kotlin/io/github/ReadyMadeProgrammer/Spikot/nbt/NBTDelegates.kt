package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.NBTTagCompound
import io.github.ReadyMadeProgrammer.Spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TagDelegate<T : Any>(
    private val check: NBTTagCompound.(String) -> Boolean,
    private val getter: NBTTagCompound.(String) -> T,
    private val setter: NBTTagCompound.(String, T) -> Unit
) : ReadWriteProperty<NBTAccessor, T?> {
    constructor(type: TagType, getter: NBTTagCompound.(String) -> T, setter: NBTTagCompound.(String, T) -> Unit) : this(
        { hasKeyOfType(it, type.id) },
        getter,
        setter
    )

    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (!thisRef.nbtTagCompound.check(property.name)) {
            return thisRef.nbtTagCompound.getter(property.name)
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.nbtTagCompound.remove(property.name)
        } else {
            thisRef.nbtTagCompound.setter(property.name, value)
        }
    }

    fun <R : Any> convert(converter: Converter<T, R>): TagDelegate<R> = TagDelegate(
        check,
        { converter.read(getter(it)) },
        { name, value -> setter(name, converter.write(value)) }
    )

    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@TagDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@TagDelegate.setValue(thisRef, property, value)
            }
        }
}

class CompoundDelegate<T : NBTAccessor>(private val constructor: (NBTTagCompound) -> T) :
    ReadWriteProperty<NBTAccessor, T?> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (!thisRef.nbtTagCompound.hasKeyOfType(property.name, TagType.TAG.id)) {
            return constructor(thisRef.nbtTagCompound.getCompound(property.name))
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.nbtTagCompound.remove(property.name)
        } else {
            thisRef.nbtTagCompound.setCompound(property.name, value.nbtTagCompound)
        }
    }

    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@CompoundDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@CompoundDelegate.setValue(thisRef, property, value)
            }
        }
}

class EnumWrappingDelegate<T : Enum<T>>(
    enumClass: KClass<T>
) : ReadWriteProperty<NBTAccessor, T?> {
    private val constants: Array<T> = enumClass.java.enumConstants
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (thisRef.nbtTagCompound.hasKeyOfType(property.name, TagType.INT.id)) {
            return constants[thisRef.nbtTagCompound.getInt(property.name)]
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.nbtTagCompound.remove(property.name)
        } else {
            thisRef.nbtTagCompound.setInt(property.name, value.ordinal)
        }
    }

    val notnull: ReadWriteProperty<NBTAccessor, T>
        get() = object : ReadWriteProperty<NBTAccessor, T> {
            override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T {
                return this@EnumWrappingDelegate.getValue(thisRef, property)!!
            }

            override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T) {
                return this@EnumWrappingDelegate.setValue(thisRef, property, value)
            }
        }
}