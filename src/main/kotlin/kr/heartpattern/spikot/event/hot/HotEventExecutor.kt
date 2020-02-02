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

import kr.heartpattern.spikot.event.getEventListeners
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.utils.catchAll
import mu.KotlinLogging
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.RegisteredListener
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.reflect.KClass

class HotEventExecutorGroup<E : Event>(val type: KClass<E>) {
    private val executors = EnumMap<EventPriority, HotEventExecutor<E>>(EventPriority::class.java)

    init {
        val handlerList = type.getEventListeners()
        for (priority in enumValues<EventPriority>()) {
            val executor = HotEventExecutor<E>()
            val listener = RegisteredListener(
                object : Listener {},
                executor,
                priority,
                spikot,
                false
            )
            handlerList.register(listener)
            executors[priority] = executor
        }
    }

    fun register(listener: HotEventListener<E>) {
        listener.executor = this
        executors[listener.priority]!!.register(listener)
    }

    fun register(priority: EventPriority, ignoreCancelled: Boolean, listener: (E, HotEventListener<E>) -> Unit): HotEventListener<E> {
        val hel = HotEventListener(
            priority = priority,
            ignoreCancelled = ignoreCancelled,
            listener = listener
        )
        register(hel)
        return hel
    }

    fun unregister(listener: HotEventListener<E>) {
        executors[listener.priority]!!.unregister(listener)
    }
}

class HotEventExecutor<E : Event> : EventExecutor {
    private val logger = KotlinLogging.logger { }
    private val listeners = LinkedHashMap<UUID, HotEventListener<E>>()
    val removeQueue = LinkedBlockingQueue<UUID>()
    override fun execute(listener: Listener?, event: Event) {
        var remove = removeQueue.poll()
        while (remove != null) {
            val removed = listeners.remove(remove)
            removed?.isRegistered = false
            remove = removeQueue.poll()
        }

        for (l in listeners.values) {
            logger.catchAll("Error while handling event: ${event::class.simpleName}") {
                if (!l.ignoreCancelled || (event as? Cancellable)?.isCancelled != true)
                    l.listener(event as E, l)
            }
        }
    }

    fun register(listener: HotEventListener<E>) {
        if (listeners.containsKey(listener.id))
            throw IllegalArgumentException("Event listener already registered")

        listeners[listener.id] = listener
        listener.isRegistered = true
    }

    fun unregister(listener: HotEventListener<E>) {
        removeQueue.add(listener.id)
    }
}

data class HotEventListener<E : Event>(
    val id: UUID = UUID.randomUUID(),
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
    val listener: (event: E, self: HotEventListener<E>) -> Unit
) {
    internal lateinit var executor: HotEventExecutorGroup<E>
    var isRegistered: Boolean = false
        internal set

    fun unregister() {
        if (!isRegistered)
            throw IllegalStateException("Already unregistered listener")
        executor.unregister(this)
    }
}