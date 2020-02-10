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

package kr.heartpattern.spikot.persistence.repository.keyvalue

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import kr.heartpattern.spikot.serialization.SerializeType

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class KeyValueRepository<K : Any, V : Any>(
    storageFactory: KeyValueStorageFactory,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    protected val storage: MutableMap<K, V> = HashMap(),
    namespace: String? = null
) : AbstractKeyValueRepository<K, V>(
    storageFactory,
    keySerializer,
    valueSerializer,
    namespace
), MutableMap<K, V> by storage {
    override fun onEnable() {
        runBlocking {
            storage.putAll(
                persistenceManager.loadAll(persistenceManager.getAllKeys())
                    .map { (key, value) ->
                        key to value.getOrNull()!!
                    }
            )
        }
    }

    override fun onDisable() {
        runBlocking {
            val new = storage.keys
            val old = persistenceManager.getAllKeys()
            persistenceManager.saveAll(
                storage.mapValues { it.value.option }
            )

            persistenceManager.saveAll((old - new).map { it to None }.toMap())
        }
    }
}