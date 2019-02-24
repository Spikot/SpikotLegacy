package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.NBTTagCompound
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TagDelegate<T>(
        private val check: NBTTagCompound.(String) -> Boolean,
        private val getter: NBTTagCompound.(String) -> T,
        private val setter: NBTTagCompound.(String, T) -> Unit
) : ReadWriteProperty<NbtAccessor, T> {
    constructor(type: TagType, getter: NBTTagCompound.(String) -> T, setter: NBTTagCompound.(String, T) -> Unit) : this(
            { hasKeyOfType(it, type.id) },
            getter,
            setter
    )

    override fun getValue(thisRef: NbtAccessor, property: KProperty<*>): T {
        val value = thisRef.nbtTagCompound.getter(property.name)
        if (!thisRef.nbtTagCompound.check(property.name)) {
            thisRef.nbtTagCompound.setter(property.name, value)
        }
        return value
    }

    override fun setValue(thisRef: NbtAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setter(property.name, value)
    }
}

class CompoundDelegate<T : NbtAccessor>(private val constructor: (NBTTagCompound) -> T) :
        ReadWriteProperty<NbtAccessor, T> {
    override fun getValue(thisRef: NbtAccessor, property: KProperty<*>): T {
        val value = thisRef.nbtTagCompound.getCompound(property.name)
        if (!thisRef.nbtTagCompound.hasKeyOfType(property.name, TagType.TAG.id)) {
            thisRef.nbtTagCompound.setCompound(property.name, value)
        }
        return constructor(value)
    }

    override fun setValue(thisRef: NbtAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setCompound(property.name, value.nbtTagCompound)
    }
}

class EnumWrappingDelegate<T : Enum<T>>(
        enumClass: KClass<T>
) : ReadWriteProperty<NbtAccessor, T> {
    private val constants: Array<T> = enumClass.java.enumConstants
    override fun getValue(thisRef: NbtAccessor, property: KProperty<*>): T {
        return constants[thisRef.nbtTagCompound.getInt(property.name)]
    }

    override fun setValue(thisRef: NbtAccessor, property: KProperty<*>, value: T) {
        thisRef.nbtTagCompound.setInt(property.name, value.ordinal)
    }
}