package kr.heartpattern.spikot.event

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

/**
 * Listener which can unsubscribe itself
 */
interface SelfCancellableEventListener : Listener {
    /**
     * Unsubscribe this listener
     */
    fun cancel() {
        HandlerList.unregisterAll(this)
    }
}