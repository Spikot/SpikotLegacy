package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.just
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.serialization.jsonSerializer
import java.io.File

class FileKeyValueStorage<K, V>(
    private val directory: File,
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>
) : KeyValueStorage<K, V> {

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