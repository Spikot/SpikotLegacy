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

package kr.heartpattern.spikot.persistence.repository.keyvalue

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.repository.AbstractRepository
import kr.heartpattern.spikot.persistence.repository.emptyKeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class KeyValueRepository<K : Any, V : Any>(
    storage: KeyValueStorage<K, V>,
    protected val holder: MutableMap<K, V> = HashMap()
) : AbstractRepository<KeyValueStorage<K, V>>(), MutableMap<K, V> by holder {
    final override var storage: KeyValueStorage<K, V> = storage
        private set

    @Deprecated("Create storage directly")
    constructor(
        storageFactory: KeyValueStorageFactory,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>,
        holder: MutableMap<K, V> = HashMap(),
        namespace: String? = null
    ) : this(emptyKeyValueStorage(), holder) {
        this.storage = storageFactory.createKeyValueStorage(
            namespace ?: this::class.qualifiedName!!,
            keySerializer,
            valueSerializer
        )
    }


    override fun onEnable() {
        runBlocking {
            putAll(
                storage.loadAll(storage.getAllKeys())
                    .map { (key, value) ->
                        key to value.getOrNull()!!
                    }
            )
        }
        super.onEnable()
    }

    override fun onDisable() {
        runBlocking {
            val new = keys
            val old = storage.getAllKeys()
            storage.saveAll(
                mapValues { it.value.option }
            )

            storage.saveAll((old - new).map { it to None }.toMap())
        }
        super.onDisable()
    }
}