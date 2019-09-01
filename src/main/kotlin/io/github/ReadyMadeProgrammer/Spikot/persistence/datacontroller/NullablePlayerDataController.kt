package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import io.github.ReadyMadeProgrammer.Spikot.persistence.gson.gson
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass

open class NullablePlayerDataController<V : Any> : DataController<UUID, V>, MutableMap<UUID, V>, Listener {
    protected lateinit var root: File
    protected lateinit var valueType: KClass<*>
    protected val value: HashMap<UUID, V> = HashMap()
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

    protected open fun load(key: UUID): V? {
        val file = File(root, "$key.json")
        return if (file.exists()) {
            try {
                val reader = FileReader(file)
                val r = gson.fromJson(reader, valueType.java) as V
                reader.close()
                r
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    protected open fun save(key: UUID, value: V?) {
        val file = File(root, "$key.json")
        if (value == null) {
            file.delete()
        }
        try {
            file.createNewFile()
            val writer = FileWriter(file)
            gson.toJson(value, valueType.java, FileWriter(file))
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        {
            val v = load(event.player.uniqueId)
            if (v != null) {
                value[event.player.uniqueId] = v
            }
        }
        Plugin.async { }
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        {
            save(event.player.uniqueId, value[event.player.uniqueId])
        }
        Plugin.async { }
    }

    final override val size: Int = value.size

    final override fun containsKey(key: UUID): Boolean {
        return key in value
    }

    final override fun containsValue(value: V): Boolean {
        return this.value.containsValue(value)
    }

    override operator fun get(key: UUID): V? {
        return value[key]
    }

    open operator fun set(key: UUID, value: V?): V? {
        val prev = this.value[key]
        if (value != null) {
            this.value[key] = value
        } else {
            this.value.remove(key)
        }
        return prev
    }

    final override fun isEmpty(): Boolean {
        return value.isEmpty()
    }

    final override val entries: MutableSet<MutableMap.MutableEntry<UUID, V>> = value.entries
    final override val keys: MutableSet<UUID> = value.keys
    final override val values: MutableCollection<V> = value.values
    override fun clear() {
        value.clear()
    }

    override fun put(key: UUID, value: V): V? {
        return this.value.put(key, value)
    }

    override fun putAll(from: Map<out UUID, V>) {
        this.value.putAll(from)
    }

    override fun remove(key: UUID): V? {
        return this.value.remove(key)
    }
}