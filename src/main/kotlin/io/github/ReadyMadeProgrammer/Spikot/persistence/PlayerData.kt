@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.persistence

import io.github.ReadyMadeProgrammer.Spikot.gson.gson
import io.github.ReadyMadeProgrammer.Spikot.logger
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import io.github.ReadyMadeProgrammer.Spikot.utils.get
import io.github.ReadyMadeProgrammer.Spikot.utils.has
import io.github.ReadyMadeProgrammer.Spikot.utils.set
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PlayerData

inline fun <reified T> Player.get(): T {
    return this["pd$${T::class.java.name}"]
}

inline fun <reified T> Player.set(data: T) {
    this["pd$${T::class.java.name}"] = data
}

internal object PlayerDataManager : Listener {
    private val dataClass = HashSet<KClass<*>>()
    private val path = File(spikotPlugin.dataFolder, "playerData")
    internal fun setup(classes: Collection<KClass<*>>) {
        if (!path.exists()) {
            path.mkdir()
        }
        dataClass.addAll(classes)
    }

    internal fun onServerOpen() {
        Bukkit.getOnlinePlayers().forEach(::load)
    }

    internal fun onServerStop() {
        Bukkit.getOnlinePlayers().forEach(::save)
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        load(event.player)
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        save(event.player)
    }

    private fun load(player: Player) {
        dataClass.forEach {
            try {
                val text = createFile(player, it).readText()
                player["pd$${it.java.name}"] = if (text.isBlank()) {
                    it.createInstance()
                } else {
                    gson.fromJson(text, it.java)
                }
            } catch (e: Exception) {
                logger.warn(e) { "Exception while loading player data: ${it.simpleName}" }
            }
        }
    }

    private fun save(player: Player) {
        dataClass.forEach {
            try {
                if (player.has("pd$${it.java.name}")) {
                    createFile(player, it).writeText(gson.toJson(player["pd$${it.java.name}"]))
                }
            } catch (e: Exception) {
                logger.warn(e) { "Exception while saving player data: ${it.simpleName}" }
            }
        }
    }

    private fun createFile(player: Player, type: KClass<*>): File {
        val middle = File(path, player.uniqueId.toString())
        if (!middle.exists()) middle.mkdir()
        val final = File(middle, "${type.qualifiedName}.json")
        if (!final.exists()) final.createNewFile()
        return final
    }
}