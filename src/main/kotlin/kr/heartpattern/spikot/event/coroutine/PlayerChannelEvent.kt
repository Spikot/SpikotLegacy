/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <reified E : PlayerEvent> Player.listenPlayerEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E> {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        if (event.player == this) {
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

@Suppress("unused")
@Deprecated(
    "Use without receiver",
    ReplaceWith("listenPlayerEvent(player, priority, ignoreCancelled")
)
inline fun <reified E : PlayerEvent> SpikotPlugin.listenPlayerEvent(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E> {
    return player.listenPlayerEvent(priority, ignoreCancelled)
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <reified E> Player.consumePlayerEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E>
    where E : PlayerEvent, E : Cancellable {
    val channel = Channel<E>()
    val listener = HotEventManager.register<E>(priority, ignoreCancelled) { event, _ ->
        if (event.player == this) {
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

@Suppress("unused")
@Deprecated(
    "Use without receiver",
    ReplaceWith("player.consumePlayerEvent(priority, ignoreCancelled")
)
inline fun <reified E> SpikotPlugin.consumePlayerEvent(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E>
    where E : PlayerEvent, E : Cancellable {
    return player.consumePlayerEvent(priority, ignoreCancelled)
}