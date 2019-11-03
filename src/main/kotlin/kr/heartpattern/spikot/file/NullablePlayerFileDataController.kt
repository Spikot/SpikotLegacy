package kr.heartpattern.spikot.file

import kr.heartpattern.spikot.gson.gson
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.thread.runAsync
import net.jodah.typetools.TypeResolver
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
open class NullablePlayerFileDataController<V : Any> : FileDataController(), MutableMap<UUID, V> {
    private val valueType: KClass<V> = TypeResolver.resolveRawArgument(NullablePlayerFileDataController::class.java, this::class.java).kotlin as KClass<V>
    protected val map: HashMap<UUID, V> = HashMap()

    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        Bukkit.getOnlinePlayers().forEach {
            val value = load(it.uniqueId)
            if (value != null)
                map[it.uniqueId] = value
        }
    }

    override fun onDisable() {
        map.forEach { (key, value) ->
            save(key, value)
        }
    }

    protected open fun load(key: UUID): V? {
        val file = File(directory, "$key.json")
        return if (file.exists()) {
            try {
                FileReader(file).use { reader ->
                    gson.fromJson(reader, valueType.java) as V
                }
            } catch (e: Exception) {
                logger.error(e) { "Cannot load player file data: $key" }
                null
            }
        } else {
            null
        }
    }

    protected open fun save(key: UUID, value: V?) {
        val file = File(directory, "$key.json")
        if (value == null) {
            file.delete()
        }
        try {
            file.createNewFile()
            FileWriter(file).use { writer ->
                gson.toJson(value, valueType.java, writer)
            }
        } catch (e: Exception) {
            logger.error(e) { "Error occured while saving player data: $key" }
        }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.runAsync {
            val v = load(event.player.uniqueId)
            if (v != null) {
                map[event.player.uniqueId] = v
            }
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
        return this.map.containsValue(value)
    }

    override operator fun get(key: UUID): V? {
        return map[key]
    }

    open operator fun set(key: UUID, value: V?): V? {
        val prev = this.map[key]
        if (value != null) {
            this.map[key] = value
        } else {
            this.map.remove(key)
        }
        return prev
    }

    final override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    final override val entries: MutableSet<MutableMap.MutableEntry<UUID, V>> = map.entries
    final override val keys: MutableSet<UUID> = map.keys
    final override val values: MutableCollection<V> = map.values
    override fun clear() {
        map.clear()
    }

    override fun put(key: UUID, value: V): V? {
        return this.map.put(key, value)
    }

    override fun putAll(from: Map<out UUID, V>) {
        this.map.putAll(from)
    }

    override fun remove(key: UUID): V? {
        return this.map.remove(key)
    }
}