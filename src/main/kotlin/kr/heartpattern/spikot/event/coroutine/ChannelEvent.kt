package kr.heartpattern.spikot.event.coroutine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.event.hot.HotEventManager
import kr.heartpattern.spikot.spikot
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E : Event> SpikotPlugin.listenEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E> {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        spikot.launch {
            channel.send(event)
        }
    }
    channel.invokeOnClose {
        listener.unregister()
    }
    return channel
}

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E> SpikotPlugin.consumeEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Channel<E>
    where E : Event, E : Cancellable {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        event.isCancelled = true
        spikot.launch {
            channel.send(event)
        }
    }
    channel.invokeOnClose {
        listener.unregister()
    }
    return channel
}