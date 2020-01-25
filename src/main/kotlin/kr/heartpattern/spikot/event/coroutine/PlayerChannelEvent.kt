package kr.heartpattern.spikot.event.coroutine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.event.hot.HotEventManager
import kr.heartpattern.spikot.spikot
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerEvent

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E : PlayerEvent> SpikotPlugin.listenPlayerEvent(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E> {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        if (event.player == player) {
            spikot.launch {
                channel.send(event)
            }
        }
    }
    channel.invokeOnClose {
        listener.unregister()
    }
    return channel
}

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E> SpikotPlugin.consumePlayerEvent(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E>
    where E : PlayerEvent, E : Cancellable {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        if (event.player == player) {
            event.isCancelled = true
            spikot.launch {
                channel.send(event)
            }
        }
    }
    channel.invokeOnClose {
        listener.unregister()
    }
    return channel
}