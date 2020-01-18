package kr.heartpattern.spikot.persistence.repository.keyvalue.player

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
import kr.heartpattern.spikot.persistence.repository.keyvalue.AbstractKeyValueRepository
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
    storageFactory: KeyValueStorageFactory,
    valueSerializer: KSerializer<V>,
    protected val default: (UUID) -> V,
    protected val onlineStorage: MutableMap<UUID, V> = HashMap(),
    offlineCacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES),
    namespace: String? = null
) : AbstractKeyValueRepository<UUID, V>(
    storageFactory,
    UUIDSerializer,
    valueSerializer,
    namespace
) {
    protected val offlineStorage = offlineCacheBuilder.removalListener { notification: RemovalNotification<UUID, V> ->
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
    }

    override fun onDisable() {
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
        return persistenceManager.load(uuid).getOrElse { default(uuid) }
    }

    private suspend inline fun save(uuid: UUID, value: V) {
        persistenceManager.save(uuid, value.option)
    }
}