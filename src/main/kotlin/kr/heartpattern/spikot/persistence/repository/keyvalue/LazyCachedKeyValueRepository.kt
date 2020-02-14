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

import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import com.google.common.cache.RemovalNotification
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.repository.AbstractRepository
import kr.heartpattern.spikot.persistence.repository.emptyKeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import java.util.concurrent.TimeUnit

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class LazyCachedKeyValueRepository<K, V : Any>(
    storage: KeyValueStorage<K, V>,
    cacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES)
) : AbstractRepository<KeyValueStorage<K, V>>() {
    final override var storage: KeyValueStorage<K, V> = storage
        private set

    protected val cache = cacheBuilder.removalListener { notification: RemovalNotification<K, Option<V>> ->
        if (notification.key != null && notification.value != null && notification.cause != RemovalCause.REPLACED) {
            plugin.launch {
                storage.save(notification.key, notification.value)
            }
        }
    }.build<K, Option<V>>()

    @Deprecated("Create storage directly")
    constructor(
        storageFactory: KeyValueStorageFactory,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>,
        cacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES),
        namespace: String?
    ) : this(emptyKeyValueStorage(), cacheBuilder) {
        this.storage = storageFactory.createKeyValueStorage(
            namespace ?: this::class.qualifiedName!!,
            keySerializer,
            valueSerializer
        )
    }

    override fun onDisable() {
        super.onDisable()
        cache.invalidateAll()
    }

    suspend fun get(key: K): V? {
        val cached = cache.getIfPresent(key)
        return if (cached != null) {
            cached.getOrNull()
        } else {
            val loaded = storage.load(key).getOrNull()
            if (loaded != null) {
                cache.put(key, loaded.option)
            }
            loaded
        }
    }

    fun set(key: K, value: V?) {
        cache.put(key, value.option)
    }
}