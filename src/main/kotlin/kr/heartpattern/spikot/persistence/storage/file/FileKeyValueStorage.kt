/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.just
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.serialization.StringSerializeFormat
import java.io.File

class FileKeyValueStorage<K, V>(
    private val directory: File,
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
    private val stringSerializeFormat: StringSerializeFormat
) : KeyValueStorage<K, V> {

    init {
        directory.mkdirs()
    }

    override suspend fun getAllKeys(): Collection<K> {
        return withContext(Dispatchers.IO) {
            directory
                .listFiles()!!
                .filter {
                    it.extension == stringSerializeFormat.fileExtensionName
                }
                .map {
                    deserialize(keySerializer, it.nameWithoutExtension)
                }
        }
    }

    override suspend fun save(key: K, value: Option<V>) {
        withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".${stringSerializeFormat.fileExtensionName}")
            if (value is Just) {
                file.createNewFile()
                file.writeText(stringSerializeFormat.serializer.stringify(valueSerializer, value.value))
            } else {
                file.delete()
            }
        }
    }

    override suspend fun load(key: K): Option<V> {
        return withContext(Dispatchers.IO) {
            val file = File(directory, serialize(keySerializer, key) + ".${stringSerializeFormat.fileExtensionName}")
            if (file.exists()) {
                stringSerializeFormat.serializer.parse(valueSerializer, file.readText()).just
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