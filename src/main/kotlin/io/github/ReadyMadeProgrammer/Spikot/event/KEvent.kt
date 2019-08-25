package io.github.ReadyMadeProgrammer.Spikot.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener

@Suppress("unused")
inline fun <reified T : Event> Plugin.subscribe(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = true,
        crossinline executable: SelfCancellableEventListener.(T) -> Unit) {
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

fun Plugin.subscribe(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, this)
