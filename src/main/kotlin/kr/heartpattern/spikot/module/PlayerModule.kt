@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.utils.nonnull
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

private object PlayerProperty : MutableProperty<Player>

/**
 * Module which is created for each player
 */
@Module(priority = ModulePriority.LOWEST)
@BaseModule
abstract class AbstractPlayerModule : AbstractModule() {
    /**
     * Owner of this module
     */
    val player: Player by contextDelegate(PlayerProperty).nonnull()
}

/**
 * Module which control player module
 * @param T Controlled player module
 * @param type Type of player module
 */
@BaseModule
open class PlayerModuleController<T : AbstractPlayerModule>(
    private val type: KClass<T>
) : AbstractModule() {
    private val table = HashMap<UUID, ModuleHandler>()

    override fun onEnable() {
        for (player in Bukkit.getOnlinePlayers()) {
            join(player)
        }
    }

    override fun onDisable() {
        for (player in Bukkit.getOnlinePlayers()) {
            quit(player)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        join(player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerQuitEvent.onQuit() {
        quit(player)
    }

    private fun join(player: Player) {
        val moduleHandler = ModuleManager.createModule(type, plugin)
        moduleHandler.context[PlayerProperty] = player
        moduleHandler.load()
        if (moduleHandler.enable())
            table[player.uniqueId] = moduleHandler
    }

    private fun quit(player: Player) {
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