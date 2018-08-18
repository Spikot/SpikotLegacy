@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import com.google.common.collect.HashBiMap
import io.github.ReadyMadeProgrammer.Spikot.Version
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

private val variableMap = mutableMapOf<UUID, MutableMap<String, Any>>()
private val legacySupport = HashBiMap.create<String, UUID>()

@Suppress("UNCHECKED_CAST")
operator fun <T> Player.get(key: String): T = variableMap[id]?.get(key) as T

@Suppress("IMPLICIT_CAST_TO_ANY")
operator fun <T> Player.set(key: String, value: T) = if (value != null) variableMap[id]?.set(key, value as Any) else variableMap.remove(id)

fun Player.has(key: String) = variableMap[id]!!.containsKey(key)

fun Player.remove(key: String) = variableMap[id]!!.remove(key)

object KPlayerListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        variableMap[e.player.id] = mutableMapOf()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(e: PlayerQuitEvent) {
        variableMap.remove(e.player.id)
    }

    fun start(plugin: Plugin) {
        Bukkit.getOnlinePlayers().forEach { variableMap[it.id] = mutableMapOf() }
        plugin.subscribe(this)
    }
}

val Player.id: UUID
    get()
    = if (version.version < Version(1, 8, 0)) {
        legacySupport[this.name] ?: let {
            val uuid = UUID.randomUUID()
            legacySupport[this.name] = uuid
            return uuid
        }
    } else {
        this.uniqueId
    }

object KPlayer {
    operator fun get(name: String): Player? = Bukkit.getPlayer(name)
    operator fun get(uuid: UUID): Player? = Bukkit.getPlayer(uuid)
}