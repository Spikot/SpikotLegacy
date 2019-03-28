package io.github.ReadyMadeProgrammer.Spikot.event

import io.github.ReadyMadeProgrammer.Spikot.module.AbstractModule
import io.github.ReadyMadeProgrammer.Spikot.module.LoadOrder
import io.github.ReadyMadeProgrammer.Spikot.module.Module
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

@Module(LoadOrder.API)
object TickEventEmitter : AbstractModule() {
    override fun onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            TickEvent().callEvent()
        }, 1L, 1L)
    }
}

class TickEvent : Event() {
    companion object {
        @JvmField
        val handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}
