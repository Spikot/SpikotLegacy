package kr.heartpattern.spikot.event

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kr.heartpattern.spikot.SpikotPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.Plugin
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend inline fun <reified E> Player.consumeNextEvent(plugin: Plugin): E
    where E : PlayerEvent, E : Cancellable = suspendCoroutine { continuation ->
    plugin.subscribe<E>(ignoreCancelled = false) { event ->
        if (event.player != this@consumeNextEvent)
            return@subscribe
        cancel()
        event.isCancelled = true
        continuation.resume(event)
    }
}

suspend inline fun <reified E : PlayerEvent> Player.listenNextEvent(plugin: Plugin): E = suspendCoroutine { continuation ->
    plugin.subscribe<E> { event ->
        if (event.player != this@listenNextEvent)
            return@subscribe
        cancel()
        continuation.resume(event)
    }
}

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified T : Event> SpikotPlugin.listenEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Channel<T> {
    val channel = Channel<T>()
    val listener = object : Listener {}
    Bukkit.getPluginManager().registerEvent(
        T::class.java,
        listener,
        priority,
        { _, event -> this.launch { channel.send(event as T) } },
        this,
        ignoreCancelled
    )
    this.subscribe(listener)
    channel.invokeOnClose {
        HandlerList.unregisterAll(listener)
    }
    return channel
}

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified T> SpikotPlugin.consumeEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Channel<T>
    where T : Event, T : Cancellable {
    val channel = Channel<T>()
    val listener = object : Listener {}
    Bukkit.getPluginManager().registerEvent(
        T::class.java,
        listener,
        priority,
        { _, event ->
            (event as T).isCancelled = true
            this.launch { channel.send(event) }
        },
        this,
        ignoreCancelled
    )
    this.subscribe(listener)
    channel.invokeOnClose {
        HandlerList.unregisterAll(listener)
    }
    return channel
}