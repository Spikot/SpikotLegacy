package kr.heartpattern.spikot.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener

/**
 * Subscribe event
 * @receiver Plugin to subscribe this event
 * @param T Event to subscribe
 * @param priority Event priority
 * @param ignoreCancelled Whether ignore event if cancelled
 * @param executable Event handler
 */
@Suppress("unused")
inline fun <reified T : Event> Plugin.subscribe(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = true,
    crossinline executable: SelfCancellableEventListener.(T) -> Unit
) {
    val handler = T::class.java.getDeclaredMethod("getHandlerList").invoke(null) as HandlerList
    val listener = object : SelfCancellableEventListener {
        override fun cancel() {
            handler.unregister(this)
        }
    }
    handler.register(
        RegisteredListener(
            listener,
            { _, event -> listener.executable(event as T) },
            priority,
            this,
            ignoreCancelled
        )
    )
}

/**
 * Subscribe listener
 * @receiver Plugin to register listener
 * @param listener Listener to register
 */
fun Plugin.subscribe(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, this)

/**
 * Call this event
 * @receiver Event to call
 */
fun Event.execute() {
    Bukkit.getServer().pluginManager.callEvent(this)
}
