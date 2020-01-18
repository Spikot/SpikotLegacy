package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule

class SingleStringDecoder(
    value: String
) : Decoder {
    private var accessed = false
    private val value: String = value
        get() {
            if (accessed)
                throw java.lang.IllegalStateException("Single string decoder cannot decode ")
            return field
        }

    override val context: SerialModule
        get() = EmptyModule
    override val updateMode: UpdateMode
        get() = UpdateMode.BANNED

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        throw UnsupportedOperationException("Cannot use structure in single string decoder")
    }

    override fun decodeBoolean(): Boolean {
        return value.toBoolean()
    }

    override fun decodeByte(): Byte {
        return value.toByte()
    }

    override fun decodeChar(): Char {
        return value[0]
    }

    override fun decodeDouble(): Double {
        return value.toDouble()
    }

    override fun decodeEnum(enumDescription: SerialDescriptor): Int {
        return value.toInt()
    }

    override fun decodeFloat(): Float {
        return value.toFloat()
    }

    override fun decodeInt(): Int {
        return value.toInt()
    }

    override fun decodeLong(): Long {
        return value.toLong()
    }

    override fun decodeNotNullMark(): Boolean {
        throw UnsupportedOperationException("Cannot decode not null mark in single string decoder")
    }

    override fun decodeNull(): Nothing? {
        accessed = true
        return null
    }

    override fun decodeShort(): Short {
        return value.toShort()
    }

    override fun decodeString(): String {
        return value
    }

    override fun decodeUnit() {
        accessed = true
    }
}