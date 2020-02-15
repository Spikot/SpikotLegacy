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

package kr.heartpattern.spikot.persistence.repository.keyvalue.player

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import com.google.common.cache.RemovalNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.getOrElse
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.repository.AbstractRepository
import kr.heartpattern.spikot.persistence.repository.emptyKeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import kr.heartpattern.spikot.serialization.serializer.UUIDSerializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.TimeUnit

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class OfflinePlayerRepository<V : Any>(
    storage: KeyValueStorage<UUID, V>,
    protected val default: (UUID) -> V,
    protected val onlineStorage: MutableMap<UUID, V> = HashMap(),
    offlineCacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES)
) : AbstractRepository<KeyValueStorage<UUID, V>>() {
    final override var storage: KeyValueStorage<UUID, V> = storage
        private set

    @Deprecated("Create storage directly")
    constructor(
        storageFactory: KeyValueStorageFactory,
        valueSerializer: KSerializer<V>,
        default: (UUID) -> V,
        onlineStorage: MutableMap<UUID, V> = HashMap(),
        offlineCacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES),
        namespace: String? = null
    ) : this(emptyKeyValueStorage(), default, onlineStorage, offlineCacheBuilder) {
        this.storage = storageFactory.createKeyValueStorage(
            namespace ?: this::class.qualifiedName!!,
            UUIDSerializer,
            valueSerializer
        )
    }

    protected val offlineStorage: Cache<UUID, V> = offlineCacheBuilder.removalListener { notification: RemovalNotification<UUID, V> ->
        if (notification.key != null && notification.value != null && notification.cause != RemovalCause.REPLACED) {
            plugin.launch {
                save(notification.key, notification.value)
            }
        }
    }.build<UUID, V>()

    override fun onEnable() {
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                onlineStorage[player.uniqueId] = load(player.uniqueId)
            }
        }
        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
        offlineStorage.invalidateAll()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        val cached = offlineStorage.getIfPresent(player.uniqueId)
        if (cached != null) {
            onlineStorage[player.uniqueId] = cached
            offlineStorage.invalidate(player.uniqueId)
        } else {
            plugin.launch {
                onlineStorage[player.uniqueId] = load(player.uniqueId)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerQuitEvent.onQuit() {
        offlineStorage.put(player.uniqueId, onlineStorage[player.uniqueId]!!)
        onlineStorage.remove(player.uniqueId)
    }

    operator fun get(player: Player): V {
        return onlineStorage[player.uniqueId]!!
    }

    suspend fun get(player: OfflinePlayer): V {
        return get(player.uniqueId)
    }

    suspend fun get(uuid: UUID): V {
        val cached = onlineStorage[uuid] ?: offlineStorage.getIfPresent(uuid)
        return cached ?: withContext(Dispatchers.IO) {
            val loaded = load(uuid)
            offlineStorage.put(uuid, loaded)
            loaded
        }
    }

    operator fun set(player: Player, value: V) {
        onlineStorage[player.uniqueId] = value
    }

    operator fun set(player: OfflinePlayer, value: V) {
        set(player.uniqueId, value)
    }

    operator fun set(uuid: UUID, value: V) {
        if (uuid in onlineStorage) {
            onlineStorage[uuid] = value
        } else {
            offlineStorage.put(uuid, value)
        }
    }

    private suspend inline fun load(uuid: UUID): V {
        return storage.load(uuid).getOrElse { default(uuid) }
    }

    private suspend inline fun save(uuid: UUID, value: V) {
        storage.save(uuid, value.option)
    }
}