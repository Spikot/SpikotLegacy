package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.just
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.serialization.SerializeType
import java.io.File

class FileKeyValueStorage<K, V>(
    private val directory: File,
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
    private val serializeType: SerializeType
) : KeyValueStorage<K, V> {

    init {
        directory.mkdirs()
    }

    override suspend fun getAllKeys(): Collection<K> {
        return withContext(Dispatchers.IO) {
            directory
                .listFiles { path, name ->
                    SerializeType.values.any {
                        val target = File(path, name)
                        target.extension == it.fileExtensionName
                    }
                }!!
                .map {
                    deserialize(keySerializer, it.nameWithoutExtension)
                }
        }
    }

    override suspend fun save(key: K, value: Option<V>) {
        withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".${serializeType.fileExtensionName}")
            if (value is Just) {
                file.createNewFile()
                file.writeText(serializeType.serialize(valueSerializer, value.value))
            } else {
                file.delete()
            }
        }
    }

    override suspend fun load(key: K): Option<V> {
        return withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".${serializeType.fileExtensionName}")
            if (file.exists()) {
                serializeType.deserialize(valueSerializer, file.readText()).just
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