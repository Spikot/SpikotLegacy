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

package kr.heartpattern.spikot.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener

/**
 * Subscribe event
 * @receiver Plugin to subscribe this event
 * @param T Event to subscribe
 * @param priority Event priority
 * @param ignoreCancelled Whether ignore event if cancelled
 * @param executable Event handler
 */
@Suppress("unused")
inline fun <reified T : Event> Plugin.subscribe(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = true,
    crossinline executable: SelfCancellableEventListener.(T) -> Unit
) {
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

/**
 * Subscribe listener
 * @receiver Plugin to register listener
 * @param listener Listener to register
 */
fun Plugin.subscribe(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, this)

/**
 * Call this event
 * @receiver Event to call
 */
fun Event.execute() {
    Bukkit.getServer().pluginManager.callEvent(this)
}
