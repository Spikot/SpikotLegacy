/*
 * Copyright 2020 HeartPattern
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

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.event.hot.HotEventManager
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.Option
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Register new event listener and suspend until callback return Just
 * @receiver SpikotPlugin which manage event listener
 * @param E Type of event
 * @param R Return type
 * @param priority EventPriority
 * @param ignoreCancelled Whether ignore event cancellation
 * @param callback Callback to listen event and produce result
 */
suspend inline fun <reified E : Event, R> suspendListenEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (E) -> Option<R>
): R = suspendCoroutine { continuation ->
    HotEventManager.register<E>(priority, ignoreCancelled) { event, self ->
        val result = callback(event)
        if (result is Just) {
            self.unregister()
            continuation.resume(result.value)
        }
    }
}

@Deprecated(
    "Use without receiver",
    ReplaceWith("suspendListenEvent(priority, ignoreCancelled, callback)")
)
suspend inline fun <reified E : Event, R> SpikotPlugin.suspendListenEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (E) -> Option<R>
): R {
    return kr.heartpattern.spikot.event.coroutine.suspendListenEvent<E, R> {
        callback(it)
    }
}