@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import com.google.common.collect.HashBiMap
import io.github.ReadyMadeProgrammer.Spikot.misc.Property
import io.github.ReadyMadeProgrammer.Spikot.module.AbstractModule
import io.github.ReadyMadeProgrammer.Spikot.module.LoadOrder
import io.github.ReadyMadeProgrammer.Spikot.module.Module
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

private val variableMap = mutableMapOf<UUID, MutableMap<String, Any>>()
private val legacySupport = HashBiMap.create<String, UUID>()

@Suppress("UNCHECKED_CAST")
@Deprecated(message = "Use property instead")
operator fun <T> Player.get(key: String): T? = variableMap[id]?.get(key) as T?

@Suppress("IMPLICIT_CAST_TO_ANY")
@Deprecated(message = "Use property instead")
operator fun <T> Player.set(key: String, value: T) {
    if (value != null)
        variableMap[id]?.set(key, value as Any)
    else
        variableMap[id]?.remove(key)
}

@Deprecated(message = "Use property instead")
fun Player.has(key: String): Boolean = variableMap[id]?.get(key) != null

@Deprecated(message = "Use property instead")
fun Player.remove(key: String) = variableMap[id]!!.remove(key)

@Suppress("UNCHECKED_CAST")
operator fun <T> Player.get(property: Property<T>): T = variableMap[id]?.get(property.key) as T

operator fun <T> Player.set(property: Property<T>, value: T) {
    if (value != null)
        variableMap[id]!![property.key] = value
    else
        variableMap[id]!!.remove(property.key)
}

operator fun <T> Player.contains(property: Property<T>): Boolean = variableMap[id]?.contains(property.key) ?: false

@Module(loadOrder = LoadOrder.API)
object KPlayerListener : AbstractModule() {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        variableMap[e.player.id] = mutableMapOf()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        variableMap.remove(e.player.id)
    }

    override fun onEnable() {
        Bukkit.getOnlinePlayers().forEach { variableMap[it.id] = mutableMapOf() }
    }
}

val Player.id: UUID
    get() = this.uniqueId