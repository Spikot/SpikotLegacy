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

/**
 * Module which is created for each player
 */
@Module(priority = ModulePriority.LOWEST)
@BaseModule
abstract class AbstractPlayerModule : AbstractModule() {
    /**
     * Owner of this module
     */
    lateinit var player: Player
        internal set
}

/**
 * Module which control player module
 * @param T Controlled player module
 * @param type Type of player module
 */
@Module(priority = ModulePriority.LOWEST)
@BaseModule
open class PlayerModuleController<T : AbstractPlayerModule>(
    private val type: KClass<T>
) : AbstractModule() {
    private val table = HashMap<UUID, ModuleHandler>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        val moduleHandler = ModuleManager.createModule(type, plugin)
        (moduleHandler.module as AbstractPlayerModule).player = player
        moduleHandler.load()
        if (moduleHandler.enable())
            table[player.uniqueId] = moduleHandler
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerQuitEvent.onQuit() {
        val module = table.remove(player.uniqueId)!!
        module.disable()
    }

    /**
     * Get module for given player
     * @param player Player to find module
     * @return Module for given player
     */
    operator fun get(player: Player): T {
        return table[player.uniqueId]!!.module!! as T
    }

    /**
     * Get module for given player
     * @param uuid UUID of online player
     * @return Module for given uuid's player
     */
    operator fun get(uuid: UUID): T {
        return table[uuid]!!.module!! as T
    }
}