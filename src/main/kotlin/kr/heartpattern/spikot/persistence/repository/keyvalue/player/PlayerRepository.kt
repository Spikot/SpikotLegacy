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

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.coroutine.delayTick
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
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Repository which store data per player.
 * Data was loaded when player join, and saved when player quit.
 * Reading offline player data does not support. For this functionality, use [OfflinePlayerRepository]
 * @param V Type of value
 * @param storageFactory KeyValueStorage factory
 * @param valueSerializer KSerializer for value type
 * @param default Default value provider if player data does not exists.
 * @param holder Online player data cache storage
 * @param namespace Namespace of data.
 */
@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class PlayerRepository<V : Any>(
    storage: KeyValueStorage<UUID, V>,
    protected val default: (Player) -> V,
    protected val holder: MutableMap<UUID, V> = HashMap()
) : AbstractRepository<KeyValueStorage<UUID, V>>(), Map<UUID, V> by holder, ReadWriteProperty<Player, V> {
    final override var storage: KeyValueStorage<UUID, V> = storage
        private set

    @Deprecated("Create storage directly")
    constructor(
        storageFactory: KeyValueStorageFactory,
        valueSerializer: KSerializer<V>,
        default: (Player) -> V,
        holder: MutableMap<UUID, V> = HashMap(),
        namespace: String? = null
    ) : this(emptyKeyValueStorage(), default, holder) {
        this.storage = storageFactory.createKeyValueStorage(
            namespace ?: this::class.qualifiedName!!,
            UUIDSerializer,
            valueSerializer
        )
    }

    override fun onEnable() {
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                load(player)
            }
        }
        super.onEnable()
    }

    override fun onDisable() {
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                save(player)
            }
        }
        super.onDisable()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        plugin.launch {
            load(player)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerQuitEvent.onQuit() {
        plugin.launch {
            save(player)
            holder.remove(player.uniqueId)
        }
    }

    /**
     * Suspend while player data is completely loaded.
     * In most case, you can just use [get] method. However, if you access data in PlayerJoinEvent, data cannot be loaded.
     * In these case use this suspension method to suspend while data is loaded.
     * @param player Player to get data.
     * @return Data for [player]
     */
    suspend fun safeGet(player: Player): V {
        while (true) { //TODO: Write a good logic
            if (player.uniqueId in holder)
                return holder[player.uniqueId]!!
            delayTick(1)
        }
    }

    operator fun get(player: Player): V {
        return this[player.uniqueId]!!
    }

    operator fun set(uuid: UUID, value: V) {
        if (uuid !in holder) throw IllegalArgumentException("Cannot set offline player's data")
        holder[uuid] = value
    }

    operator fun set(player: Player, value: V) {
        holder[player.uniqueId] = value
    }

    override fun getValue(thisRef: Player, property: KProperty<*>): V {
        return this[thisRef]
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: V) {
        this[thisRef] = value
    }

    private suspend inline fun load(player: Player) {
        holder[player.uniqueId] = storage.load(player.uniqueId).getOrElse { default(player) }
    }

    private suspend inline fun save(player: Player) {
        storage.save(player.uniqueId, holder[player.uniqueId].option)
    }
}