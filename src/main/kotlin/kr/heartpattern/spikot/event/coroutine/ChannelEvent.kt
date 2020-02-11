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
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E : Event> listenEvent(
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

@Suppress("unused")
@Deprecated(
    "Use without receiver instead",
    ReplaceWith("listenEvent(priority, ignoreCancelled)")
)
inline fun <reified E : Event> SpikotPlugin.listenEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): ReceiveChannel<E> {
    return kr.heartpattern.spikot.event.coroutine.listenEvent(priority, ignoreCancelled)
}

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <reified E> consumeEvent(
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

@Suppress("unused")
@Deprecated(
    "Use without receiver instaed",
    ReplaceWith("listenEvent(priority, ignoreCancelled)")
)
inline fun <reified E> SpikotPlugin.consumeEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Channel<E>
    where E : Event, E : Cancellable {
    return kr.heartpattern.spikot.event.coroutine.consumeEvent(priority, ignoreCancelled)
}