package kr.heartpattern.spikot.event

import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor

class SimpleEventExecutor<E : Event>(val listener: (E) -> Unit) : EventExecutor {
    override fun execute(listener: Listener?, event: Event) {
        listener(event as E)
    }
}