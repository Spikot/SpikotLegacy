package io.github.ReadyMadeProgrammer.Spikot

import com.google.common.collect.HashBiMap
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

private val variableMap = mutableMapOf<UUID,MutableMap<String,Any>>()
private val legacySupport = HashBiMap.create<String,UUID>()

fun Player.get(key: String) = variableMap[id]?.get(key)
fun Player.set(key: String, value: Any?) = if(value!=null)variableMap[id]?.set(key,value)else Unit

object KPlayerListener: Listener {
    @EventHandler(priority= EventPriority.LOWEST) fun onJoin(e: PlayerJoinEvent){
        variableMap[e.player.id] = mutableMapOf()
    }
    @EventHandler(priority= EventPriority.LOWEST) fun onQuit(e: PlayerQuitEvent){
        variableMap.remove(e.player.id)
    }
    fun start(plugin: Plugin){
        Bukkit.getOnlinePlayers().forEach{variableMap[it.id] = mutableMapOf()}
        plugin.subscribe(this)
    }
}

val Player.id: UUID
    get()
          =this.uniqueId
// if(version<Version(1,8,0)){
//            legacySupport[this.name]?:let{
//                val uuid = UUID.randomUUID()
//                legacySupport[this.name] = uuid
//                return uuid
//            }
//        }
//        else{
//            this.uniqueId
//        }