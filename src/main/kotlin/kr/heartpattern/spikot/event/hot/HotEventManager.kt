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

package kr.heartpattern.spikot.event.hot

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import kotlin.reflect.KClass

object HotEventManager {
    private val registry = HashMap<Class<out Event>, HotEventExecutorGroup<*>>()

    private fun <E : Event> getExecutorGroup(event: KClass<E>): HotEventExecutorGroup<E> {
        val registered = registry[event.java]
        if (registered != null) return registered as HotEventExecutorGroup<E>

        synchronized(this) {
            val doubleCheck = registry[event.java]
            if (doubleCheck != null) return doubleCheck as HotEventExecutorGroup<E>

            val new = HotEventExecutorGroup(event)
            registry[event.java] = new
            return new
        }
    }

    fun <E : Event> register(
        event: KClass<E>,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        listener: (E, HotEventListener<E>) -> Unit
    ): HotEventListener<E> {
        val executorGroup = getExecutorGroup(event)
        return executorGroup.register(priority, ignoreCancelled, listener)
    }

    inline fun <reified E : Event> register(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        noinline listener: (E, HotEventListener<E>) -> Unit
    ): HotEventListener<E> {
        return register(E::class, priority, ignoreCancelled, listener)
    }
}