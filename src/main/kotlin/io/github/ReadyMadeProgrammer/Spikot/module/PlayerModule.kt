package io.github.ReadyMadeProgrammer.Spikot.module

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap

interface IPlayerModule {
    fun initialize(plugin: Spikot, player: Player) {}
    fun onJoin() {}
    fun onQuit() {}
}

abstract class AbstractPlayerModule : IPlayerModule {
    protected lateinit var plugin: Spikot
        private set
    protected lateinit var player: Player
        private set

    override fun initialize(plugin: Spikot, player: Player) {
        this.plugin = plugin
        this.player = player
    }
}

open class PlayerModuleController<T : IPlayerModule>(
    private val constructor: () -> T
) : AbstractModule() {
    private val table = HashMap<UUID, T>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun PlayerJoinEvent.onJoin() {
        val module = constructor()
        module.initialize(plugin, player)
        module.onJoin()
        table[player.uniqueId] = module
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerQuitEvent.onQuit() {
        val module = table.remove(player.uniqueId)!!
        module.onQuit()
    }

    operator fun get(player: Player): T {
        return table[player.uniqueId]!!
    }

    operator fun get(uuid: UUID): T {
        return table[uuid]!!
    }
}