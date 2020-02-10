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

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.persistence.storage.*
import kr.heartpattern.spikot.serialization.SerializeType
import org.bukkit.plugin.Plugin
import java.io.File

class FileStorageFactory(val serializeType: SerializeType) : KeyValueStorageFactory, SingletonStorageFactory {
    override fun <K, V> createKeyValueStorage(
        plugin: Plugin,
        namespace: String,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>): KeyValueStorage<K, V> {
        return FileKeyValueStorage(File(plugin.dataFolder, namespace), keySerializer, valueSerializer, serializeType)
    }

    override fun <V> createSingletonStorage(
        plugin: Plugin,
        namespace: String,
        serializer: KSerializer<V>
    ): SingletonStorage<V> {
        return FileSingletonStorage(File(plugin.dataFolder, "${namespace}.${serializeType.fileExtensionName}"), serializer, serializeType)
    }
}

