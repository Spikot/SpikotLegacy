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

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import kr.heartpattern.spikot.serialization.SerializeType

abstract class AbstractKeyValueRepository<K, V>(
    protected val storageFactory: KeyValueStorageFactory,
    protected val keySerializer: KSerializer<K>,
    protected val valueSerializer: KSerializer<V>,
    protected val namespace: String? = null
) : AbstractModule() {
    protected lateinit var persistenceManager: KeyValueStorage<K, V>
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        persistenceManager = storageFactory.createKeyValueStorage(
            this.plugin,
            namespace ?: this::class.qualifiedName!!,
            keySerializer,
            valueSerializer
        )
    }
}