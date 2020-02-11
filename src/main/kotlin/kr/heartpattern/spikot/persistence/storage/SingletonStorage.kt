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

package kr.heartpattern.spikot.persistence.storage

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Option
import org.bukkit.plugin.Plugin

/**
 * Persistence delegate. This class do actual key-value data persist.
 * @param V Type of value
 */
interface SingletonStorage<V> : Storage {
    /**
     * Load data from persistence storage.
     * @return Loaded value
     */
    suspend fun get(): Option<V>

    /**
     * Save data to persistence storage.
     * @param value Value to save
     */
    suspend fun set(value: Option<V>)
}

interface SingletonStorageFactory : StorageFactory {
    fun <V> createSingletonStorage(
        plugin: Plugin,
        namespace: String,
        serializer: KSerializer<V>
    ): SingletonStorage<V>
}