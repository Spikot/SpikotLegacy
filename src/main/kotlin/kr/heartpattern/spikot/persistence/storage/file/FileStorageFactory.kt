/*
 * Copyright 2020 Spikot project authors
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

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.persistence.storage.SingletonStorageFactory
import kr.heartpattern.spikot.serialization.JsonStringSerializeFormat
import kr.heartpattern.spikot.serialization.StringSerializeFormat

@Deprecated("Create storage directly")
open class FileStorageFactory(val stringSerializeFormat: StringSerializeFormat) : KeyValueStorageFactory, SingletonStorageFactory {
    @Deprecated("Specify file format explicitly",
        ReplaceWith(
            "FileStorageFactory(StringSerializeFormat.JSON)",
            "kr.heartpattern.spikot.serialization.StringSerializerFormat"
        )
    )
    companion object : FileStorageFactory(JsonStringSerializeFormat)

    override fun <K, V> createKeyValueStorage(
        namespace: String,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>
    ): KeyValueStorage<K, V> {
        return FileKeyValueStorage(keySerializer, valueSerializer, stringSerializeFormat, namespace)
    }

    override fun <V> createSingletonStorage(
        namespace: String,
        serializer: KSerializer<V>
    ): SingletonStorage<V> {
        return FileSingletonStorage(serializer, stringSerializeFormat, namespace)
    }
}

