package kr.heartpattern.spikot.file

import kr.heartpattern.spikot.gson.gson
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.thread.runAsync
import net.jodah.typetools.TypeResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
open class PlayerFileDataController<V : Any>(protected val constructor: (UUID) -> V) : FileDataController(), Map<UUID, V> {
    private val valueType: KClass<V> = TypeResolver.resolveRawArgument(PlayerFileDataController::class.java, this::class.java).kotlin as KClass<V>
    protected val map: HashMap<UUID, V> = HashMap()

    constructor(type: KClass<V>) : this({ type.createInstance() })

    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        Bukkit.getOnlinePlayers().forEach {
            map[it.uniqueId] = load(it.uniqueId)
        }
    }

    override fun onDisable() {
        map.forEach { (key, value) ->
            save(key, value)
        }
    }

    private fun load(key: UUID): V {
        val file = File(directory, "$key.json")
        return if (!file.exists()) {
            constructor(key)
        } else {
            try {
                val reader = FileReader(file)
                val r = gson.fromJson(reader, valueType.java) as V
                reader.close()
                r
            } catch (e: Exception) {
                logger.error(e) { "Cannot load player file data: $key" }
                constructor(key)
            }
        }
    }

    private fun save(key: UUID, value: V?) {
        val file = File(directory, "$key.json")
        try {
            file.createNewFile()
            FileWriter(file).use { writer ->
                gson.toJson(value, valueType.java, writer)
            }
        } catch (e: Exception) {
            logger.error(e) { "Cannot save player file data: $key" }
        }
    }


    override operator fun get(key: UUID): V {
        if (!map.containsKey(key)) {
            map[key] = constructor(key)
        }
        return map[key]!!
    }

    operator fun get(player: Player): V {
        return this[player.uniqueId]
    }

    operator fun set(key: UUID, value: V): V? {
        val prev = map[key]
        map[key] = value
        return prev
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.runAsync {
            val v = load(event.player.uniqueId)
            map[event.player.uniqueId] = v
        }
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.runAsync {
            save(event.player.uniqueId, map[event.player.uniqueId])
        }
    }

    final override val size: Int = map.size

    final override fun containsKey(key: UUID): Boolean {
        return key in map
    }

    final override fun containsValue(value: V): Boolean {
        return map.containsValue(value)
    }

    final override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    final override val entries: Set<MutableMap.MutableEntry<UUID, V>> = map.entries
    final override val keys: Set<UUID> = map.keys
    final override val values: Collection<V> = map.values
}