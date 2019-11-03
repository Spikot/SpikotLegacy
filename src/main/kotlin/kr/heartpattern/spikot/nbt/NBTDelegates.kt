package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TagDelegate<T : Any>(
    val check: WrapperNBTCompound.(String) -> Boolean,
    val getter: WrapperNBTCompound.(String) -> T,
    val setter: WrapperNBTCompound.(String, T) -> Unit
) : ReadWriteProperty<NBTAccessor, T?> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (!thisRef.tag.check(property.name)) {
            return thisRef.tag.getter(property.name)
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag.setter(property.name, value)
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

class CompoundDelegate<T : NBTAccessor>(private val constructor: (WrapperNBTCompound) -> T) :
    ReadWriteProperty<NBTAccessor, T?> {
    override fun getValue(thisRef: NBTAccessor, property: KProperty<*>): T? {
        if (!thisRef.tag.hasKeyOfType(property.name, TagType.COMPOUND)) {
            return constructor(thisRef.tag.getNBTCompound(property.name))
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag[property.name] = value.tag
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
        if (thisRef.tag.hasKeyOfType(property.name, TagType.INT)) {
            return constants[thisRef.tag.getInt(property.name)]
        }
        return null
    }

    override fun setValue(thisRef: NBTAccessor, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.tag.remove(property.name)
        } else {
            thisRef.tag.setInt(property.name, value.ordinal)
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