package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule

class SingleStringEncoder : Encoder {
    var encoded: String? = null
        get() {
            if (field == null) {
                throw IllegalStateException("Single string encoder encode nothing.")
            }
            return field
        }
        set(value) {
            if (field != null) {
                throw IllegalStateException("Single string encoder accept only one encoding.")
            }
            field = value
        }

    override val context: SerialModule
        get() = EmptyModule

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        throw UnsupportedOperationException("Cannot use structure in single string encoder")
    }

    override fun encodeBoolean(value: Boolean) {
        encoded = value.toString()
    }

    override fun encodeByte(value: Byte) {
        encoded = value.toString()
    }

    override fun encodeChar(value: Char) {
        encoded = value.toString()
    }

    override fun encodeDouble(value: Double) {
        encoded = value.toString()
    }

    override fun encodeEnum(enumDescription: SerialDescriptor, ordinal: Int) {
        encoded = ordinal.toString()
    }

    override fun encodeFloat(value: Float) {
        encoded = value.toString()
    }

    override fun encodeInt(value: Int) {
        encoded = value.toString()
    }

    override fun encodeLong(value: Long) {
        encoded = value.toString()
    }

    override fun encodeNotNullMark() {
        throw UnsupportedOperationException("Cannot mark not null in single string encoder")
    }

    override fun encodeNull() {
        encoded = ""
    }

    override fun encodeShort(value: Short) {
        encoded = value.toString()
    }

    override fun encodeString(value: String) {
        encoded = value
    }

    override fun encodeUnit() {
        encoded = Unit.toString()
    }
}