package kr.heartpattern.spikot.repository.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.just
import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.repository.Repository
import kr.heartpattern.spikot.serialization.jsonSerializer
import java.io.File

class FileStorage<K, V>(
    namespace: String,
    enclosure: Repository<K, V>,
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>
) : Storage<K, V> {
    private val directory = File(
        enclosure.context[IModule.PluginProperty]!!.dataFolder,
        namespace
    )

    init {
        directory.mkdirs()
    }

    override suspend fun getAllKeys(): Collection<K> {
        return withContext(Dispatchers.IO) {
            directory
                .listFiles { _, name -> name.endsWith(".json") }!!
                .map {
                    deserialize(keySerializer, it.name.substring(0 until it.name.length - 5))
                }
        }
    }

    override suspend fun save(key: K, value: Option<V>) {
        withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".json")
            if (value is Just) {
                file.createNewFile()
                file.writeText(jsonSerializer.stringify(valueSerializer, value.value))
            } else {
                file.delete()
            }
        }
    }

    override suspend fun load(key: K): Option<V> {
        return withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".json")
            if (file.exists()) {
                jsonSerializer.parse(valueSerializer, file.readText()).just
            } else {
                None
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            directory.listFiles(File::delete)
        }
    }

    private fun <T> serialize(serializer: KSerializer<T>, value: T): String {
        val encoder = SingleStringEncoder()
        serializer.serialize(encoder, value)
        return encoder.encoded!!
    }

    private fun <T> deserialize(serializer: KSerializer<T>, value: String): T {
        val decoder = SingleStringDecoder(value)
        return serializer.deserialize(decoder)
    }
}

object FileStorageFactory : StorageFactory {
    override fun <K, V> createPersistenceManager(
        namespace: String,
        enclosure: Repository<K, V>,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>
    ): Storage<K, V> {
        return FileStorage(namespace, enclosure, keySerializer, valueSerializer)
    }
}

private class SingleStringEncoder : Encoder {
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

private class SingleStringDecoder(
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