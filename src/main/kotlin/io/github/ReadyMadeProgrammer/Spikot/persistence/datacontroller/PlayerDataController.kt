package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import io.github.ReadyMadeProgrammer.Spikot.persistence.gson.gson
import io.github.ReadyMadeProgrammer.Spikot.thread.async
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class PlayerDataController<V : Any>(protected val constructor: (UUID) -> V) : DataController<UUID, V>, Map<UUID, V>, Listener {
    protected lateinit var root: File
    protected lateinit var valueType: KClass<*>
    protected val value: HashMap<UUID, V> = HashMap()

    constructor(type: KClass<V>) : this({ type.createInstance() })

    final override fun initialize(directory: File, valueType: KClass<*>) {
        root = File(directory, valueType.qualifiedName)
        if (!root.exists()) {
            root.mkdirs()
        }
        this.valueType = valueType
        Bukkit.getOnlinePlayers().forEach {
            load(it.uniqueId)
        }
    }

    final override fun destroy() {
        value.forEach { (key, value) ->
            save(key, value)
        }
    }

    private fun load(key: UUID): V {
        val file = File(root, "$key.json")
        return if (!file.exists()) {
            constructor(key)
        } else {
            try {
                val reader = FileReader(file)
                @Suppress("UNCHECKED_CAST")
                val r = gson.fromJson(reader, valueType.java) as V
                reader.close()
                r
            } catch (e: Exception) {
                e.printStackTrace()
                constructor(key)
            }
        }

    }

    private fun save(key: UUID, value: V?) {
        val file = File(root, "$key.json")
        try {
            file.createNewFile()
            val writer = FileWriter(file)
            gson.toJson(value, valueType.java, writer)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override operator fun get(key: UUID): V {
        if (!value.containsKey(key)) {
            this.value[key] = constructor(key)
        }
        return this.value[key]!!
    }

    operator fun get(player: Player): V {
        return this[player.uniqueId]
    }

    operator fun set(key: UUID, value: V): V? {
        val prev = this.value[key]
        this.value[key] = value
        return prev
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        async {
            val v = load(event.player.uniqueId)
            value[event.player.uniqueId] = v
        }
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        async {
            save(event.player.uniqueId, value[event.player.uniqueId])
        }
    }

    final override val size: Int = value.size

    final override fun containsKey(key: UUID): Boolean {
        return key in value
    }

    final override fun containsValue(value: V): Boolean {
        return this.value.containsValue(value)
    }

    final override fun isEmpty(): Boolean {
        return value.isEmpty()
    }

    final override val entries: Set<MutableMap.MutableEntry<UUID, V>> = value.entries
    final override val keys: Set<UUID> = value.keys
    final override val values: Collection<V> = value.values
}