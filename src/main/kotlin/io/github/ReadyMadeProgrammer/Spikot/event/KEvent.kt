package io.github.ReadyMadeProgrammer.Spikot.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

@Suppress("unused")
inline fun <reified T : Event> Plugin.subscribe(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = true,
        crossinline executable: SelfCancellableEventListener.(T) -> Unit) {
    val listener = object : SelfCancellableEventListener {}
    Bukkit.getPluginManager()
            .registerEvent(
                    T::class.java,
                    listener,
                    priority,
                    { cancel, event -> (cancel as SelfCancellableEventListener).executable(event as T) },
                    this,
                    ignoreCancelled)
}

fun Plugin.subscribe(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, this)
