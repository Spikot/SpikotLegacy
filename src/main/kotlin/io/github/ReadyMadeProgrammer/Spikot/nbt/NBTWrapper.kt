package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.*
import net.minecraft.server.v1_12_R1.NBTTagEnd
import java.util.*

interface WrapperNBTBase<out B : NBTBase, out T : Any> {
    val tag: B
    val typeId: Byte
        get() = tag.typeId
    val value: T
}

interface WrapperNBTNumber<B : NBTBase, T : Number> : WrapperNBTBase<B, T> {
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

private val constructorNBTTagEnd = NBTTagEnd::class.java.getDeclaredConstructor().apply { isAccessible = true }

class WrapperNBTTagEnd(override val tag: NBTTagEnd) : WrapperNBTBase<NBTTagEnd, Unit> {
    constructor() : this(constructorNBTTagEnd.newInstance())

    override val value: Unit
        get() = Unit
}

class WrapperNBTTagByte(override val tag: NBTTagByte) : WrapperNBTNumber<NBTTagByte, Byte> {
    constructor(value: Byte) : this(NBTTagByte(value))

    override val value: Byte
        get() = tag.g()
}

class WrapperNBTTagLong(override val tag: NBTTagLong) : WrapperNBTNumber<NBTTagLong, Long> {
    constructor(value: Long) : this(NBTTagLong(value))

    override val value: Long
        get() = tag.d()
}

class WrapperNBTTagInt(override val tag: NBTTagInt) : WrapperNBTNumber<NBTTagInt, Int> {
    constructor(value: Int) : this(NBTTagInt(value))

    override val value: Int
        get() = tag.e()
}

class WrapperNBTTagShort(override val tag: NBTTagShort) : WrapperNBTNumber<NBTTagShort, Short> {
    constructor(value: Short) : this(NBTTagShort(value))

    override val value: Short
        get() = tag.f()
}

class WrapperNBTTagDouble(override val tag: NBTTagDouble) : WrapperNBTNumber<NBTTagDouble, Double> {
    constructor(value: Double) : this(NBTTagDouble(value))

    override val value: Double
        get() = tag.asDouble()
}

class WrapperNBTTagFloat(override val tag: NBTTagFloat) : WrapperNBTNumber<NBTTagFloat, Float> {
    constructor(value: Float) : this(NBTTagFloat(value))

    override val value: Float
        get() = tag.i()
}

class WrapperNBTTagByteArray(override val tag: NBTTagByteArray) : WrapperNBTBase<NBTTagByteArray, ByteArray> {
    constructor(value: ByteArray) : this(NBTTagByteArray(value))

    override val value: ByteArray
        get() = tag.c()
}

class WrapperNBTTagIntArray(override val tag: NBTTagIntArray) : WrapperNBTBase<NBTTagIntArray, IntArray> {
    constructor(value: IntArray) : this(NBTTagIntArray(value))

    override val value: IntArray
        get() = tag.d()
}

class WrapperNBTTagLongArray(override val tag: NBTTagLongArray) : WrapperNBTBase<NBTTagLongArray, LongArray> {
    constructor(value: LongArray) : this(NBTTagLongArray(value))

    override val value: LongArray
        get() = tag.get()
}

class WrapperNBTTagString(override val tag: NBTTagString) : WrapperNBTBase<NBTTagString, String> {
    constructor(value: String) : this(NBTTagString(value))

    override val value: String
        get() = tag.c_()
}

class WrapperNBTTagList<T : WrapperNBTBase<*, *>>(override val tag: NBTTagList) : WrapperNBTBase<NBTTagList, MutableList<T>>, MutableList<T> {
    constructor() : this(NBTTagList())

    override val value: MutableList<T>
        get() = this
    override val size: Int
        get() = tag.size()

    override fun contains(element: T): Boolean = tag.list.contains(element.tag)

    override fun containsAll(elements: Collection<T>): Boolean = tag.list.containsAll(elements.map { it.tag })

    override fun get(index: Int): T = wrapNBT(tag[index])

    override fun indexOf(element: T): Int = tag.list.indexOf(element.tag)

    override fun isEmpty(): Boolean = tag.isEmpty

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private val iterator = tag.list.iterator()
        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T = wrapNBT(iterator.next())

        override fun remove() = iterator.remove()
    }

    override fun lastIndexOf(element: T): Int = tag.list.lastIndexOf(element.tag)

    override fun add(element: T): Boolean {
        tag.add(element.tag)
        return true
    }

    override fun add(index: Int, element: T) = tag.list.add(index, element.tag)

    override fun addAll(index: Int, elements: Collection<T>): Boolean = tag.list.addAll(index, elements.map { it.tag })

    override fun addAll(elements: Collection<T>): Boolean = tag.list.addAll(elements.map { it.tag })

    override fun clear() = tag.list.clear()

    override fun listIterator(): MutableListIterator<T> = listIterator(0)

    override fun listIterator(index: Int): MutableListIterator<T> = object : MutableListIterator<T> {
        private val iterator = tag.list.listIterator(index)
        override fun hasPrevious(): Boolean = iterator.hasPrevious()

        override fun nextIndex(): Int = iterator.nextIndex()

        override fun previous(): T = wrapNBT(iterator.previous())

        override fun previousIndex(): Int = iterator.previousIndex()

        override fun add(element: T) = iterator.add(element.tag)

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T = wrapNBT(iterator.next())

        override fun remove() = iterator.remove()

        override fun set(element: T) = iterator.set(element.tag)
    }

    override fun remove(element: T): Boolean = tag.list.remove(element.tag)

    override fun removeAll(elements: Collection<T>): Boolean = tag.list.removeAll(elements.map { it.tag })

    override fun removeAt(index: Int): T = wrapNBT(tag.remove(index))

    override fun retainAll(elements: Collection<T>): Boolean = tag.list.retainAll(elements.map { it.tag })

    override fun set(index: Int, element: T): T = wrapNBT(tag.list.set(index, element.tag))

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = tag.list.subList(fromIndex, toIndex).map { wrapNBT<T>(it) }.toMutableList()
}

class WrapperNBTTagCompound(override val tag: NBTTagCompound) : WrapperNBTBase<NBTTagCompound, MutableMap<String, WrapperNBTBase<*, *>>>, MutableMap<String, WrapperNBTBase<*, *>> {
    constructor() : this(NBTTagCompound())

    override val value: MutableMap<String, WrapperNBTBase<*, *>>
        get() = this
    override val size: Int
        get() = tag.map.size

    override fun containsKey(key: String): Boolean = tag.map.containsKey(key)

    override fun containsValue(value: WrapperNBTBase<*, *>): Boolean = tag.map.containsValue(value.tag)

    override fun get(key: String): WrapperNBTBase<*, *>? {
        val t = tag.map[key]
        return if (t == null) {
            null
        } else {
            wrapNBTByTypeId(t)
        }
    }

    override fun isEmpty(): Boolean = tag.map.isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>>
        get() = object : MutableSet<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>> {
            private val set = tag.map.entries
            private fun read(element: MutableMap.MutableEntry<String, NBTBase>): MutableMap.MutableEntry<String, WrapperNBTBase<*, *>> = AbstractMap.SimpleEntry(element.key, wrapNBT(element.value))
            private fun write(element: MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>): MutableMap.MutableEntry<String, NBTBase> = AbstractMap.SimpleEntry(element.key, element.value.tag)
            override fun add(element: MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>): Boolean = set.add(write(element))

            override fun addAll(elements: Collection<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>>): Boolean = set.addAll(elements.map(::write))

            override fun clear() = set.clear()

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>> = object : MutableIterator<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>> {
                private val iterator = set.iterator()
                override fun hasNext(): Boolean = iterator.hasNext()

                override fun next(): MutableMap.MutableEntry<String, WrapperNBTBase<*, *>> = read(iterator.next())

                override fun remove() = iterator.remove()
            }

            override fun remove(element: MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>): Boolean = set.remove(write(element))

            override fun removeAll(elements: Collection<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>>): Boolean = set.removeAll(elements.map(::write))

            override fun retainAll(elements: Collection<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>>): Boolean = set.retainAll(elements.map(::write))

            override val size: Int
                get() = set.size

            override fun contains(element: MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>): Boolean = set.contains(write(element))

            override fun containsAll(elements: Collection<MutableMap.MutableEntry<String, WrapperNBTBase<*, *>>>): Boolean = set.containsAll(elements.map(::write))

            override fun isEmpty(): Boolean = set.isEmpty()
        }
    override val keys: MutableSet<String>
        get() = tag.map.keys
    override val values: MutableCollection<WrapperNBTBase<*, *>>
        get() = object : MutableCollection<WrapperNBTBase<*, *>> {
            private val value = tag.map.values
            override val size: Int
                get() = value.size

            override fun contains(element: WrapperNBTBase<*, *>): Boolean = value.contains(element.tag)

            override fun containsAll(elements: Collection<WrapperNBTBase<*, *>>): Boolean = value.containsAll(elements.map { it.tag })

            override fun isEmpty(): Boolean = value.isEmpty()

            override fun add(element: WrapperNBTBase<*, *>): Boolean = value.add(element.tag)

            override fun addAll(elements: Collection<WrapperNBTBase<*, *>>): Boolean = value.addAll(elements.map { it.tag })

            override fun clear() = value.clear()

            override fun iterator(): MutableIterator<WrapperNBTBase<*, *>> = object : MutableIterator<WrapperNBTBase<*, *>> {
                private val iterator = value.iterator()

                override fun hasNext(): Boolean = iterator.hasNext()

                override fun next(): WrapperNBTBase<*, *> = wrapNBT(iterator.next())

                override fun remove() = iterator.remove()
            }

            override fun remove(element: WrapperNBTBase<*, *>): Boolean = value.remove(element.tag)

            override fun removeAll(elements: Collection<WrapperNBTBase<*, *>>): Boolean = value.removeAll(elements.map { it.tag })

            override fun retainAll(elements: Collection<WrapperNBTBase<*, *>>): Boolean = value.retainAll(elements.map { it.tag })
        }

    override fun clear() = tag.map.clear()

    override fun put(key: String, value: WrapperNBTBase<*, *>): WrapperNBTBase<*, *>? {
        val result = tag.map.put(key, value.tag)
        return if (result == null) {
            result
        } else {
            wrapNBT(result)
        }
    }

    override fun putAll(from: Map<out String, WrapperNBTBase<*, *>>) = tag.map.putAll(from.mapValues { it.value.tag })

    override fun remove(key: String): WrapperNBTBase<*, *>? {
        val result = tag.map.remove(key)
        return if (result == null) {
            null
        } else {
            wrapNBT(result)
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T : NBTBase> unwrapNBT(tag: WrapperNBTBase<*, *>): T {
    return tag.tag as T
}

@Suppress("UNCHECKED_CAST")
fun <T : WrapperNBTBase<*, *>> wrapNBT(tag: NBTBase): T {
    return wrapNBTByTypeId(tag) as T
}

fun wrapNBTByTypeId(tag: NBTBase): WrapperNBTBase<*, *> {
    return when (tag.typeId.toInt()) {
        0 -> WrapperNBTTagEnd(tag as NBTTagEnd)
        1 -> WrapperNBTTagByte(tag as NBTTagByte)
        2 -> WrapperNBTTagShort(tag as NBTTagShort)
        3 -> WrapperNBTTagInt(tag as NBTTagInt)
        4 -> WrapperNBTTagLong(tag as NBTTagLong)
        5 -> WrapperNBTTagFloat(tag as NBTTagFloat)
        6 -> WrapperNBTTagDouble(tag as NBTTagDouble)
        7 -> WrapperNBTTagByteArray(tag as NBTTagByteArray)
        8 -> WrapperNBTTagString(tag as NBTTagString)
        9 -> WrapperNBTTagList<WrapperNBTBase<*, *>>(tag as NBTTagList)
        10 -> WrapperNBTTagCompound(tag as NBTTagCompound)
        11 -> WrapperNBTTagIntArray(tag as NBTTagIntArray)
        12 -> WrapperNBTTagLongArray(tag as NBTTagLongArray)
        else -> throw IllegalArgumentException("Unknown type id: ${tag.typeId}")
    }
}