package kr.heartpattern.spikot.repository

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.misc.getOrElse
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.repository.persistence.StorageFactory
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

abstract class PlayerRepository<V : Any>(
    storageFactory: StorageFactory,
    valueSerializer: KSerializer<V>,
    protected val default: (Player) -> V,
    protected val storage: MutableMap<UUID, V> = HashMap(),
    namespace: String? = null
) : Repository<UUID, V>(
    storageFactory,
    UUIDSerializer,
    valueSerializer,
    namespace
), Map<UUID, V> by storage, ReadWriteProperty<Player, V> {
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                load(player)
            }
        }
    }

    override fun onDisable() {
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                save(player)
            }
        }
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
            storage.remove(player.uniqueId)
        }
    }

    operator fun get(player: Player): V {
        return this[player.uniqueId]!!
    }

    operator fun set(uuid: UUID, value: V) {
        if (uuid !in storage) throw IllegalArgumentException("Cannot set offline player's data")
        storage[uuid] = value
    }

    operator fun set(player: Player, value: V) {
        storage[player.uniqueId] = value
    }

    override fun getValue(thisRef: Player, property: KProperty<*>): V {
        return this[thisRef]
    }

    override fun setValue(thisRef: Player, property: KProperty<*>, value: V) {
        this[thisRef] = value
    }

    private suspend inline fun load(player: Player) {
        storage[player.uniqueId] = persistenceManager.load(player.uniqueId).getOrElse { default(player) }
    }

    private suspend inline fun save(player: Player) {
        persistenceManager.save(player.uniqueId, storage[player.uniqueId].option)
    }
}