@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.module

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

abstract class AbstractPlayerModule : AbstractModule() {
    lateinit var player: Player
        internal set
}

open class PlayerModuleController<T : AbstractPlayerModule>(
    private val type: KClass<T>
) : AbstractModule() {
    private val table = HashMap<UUID, ModuleHandler>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        val moduleHandler = ModuleManager.createModule(type, plugin)
        (moduleHandler.module as AbstractPlayerModule).player = player
        if (moduleHandler.enable())
            table[player.uniqueId] = moduleHandler
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerQuitEvent.onQuit() {
        val module = table.remove(player.uniqueId)!!
        module.disable()
    }

    operator fun get(player: Player): T {
        return table[player.uniqueId]!!.module!! as T
    }

    operator fun get(uuid: UUID): T {
        return table[uuid]!!.module!! as T
    }
}