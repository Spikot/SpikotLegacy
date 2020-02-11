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

package kr.heartpattern.spikot.packet

import com.comphenix.packetwrapper.AbstractPacket
import com.comphenix.protocol.events.ListenerPriority
import kotlin.reflect.KClass

/**
 * Annotate packet handler method
 * @param packets Packets to listen
 * @param async Whether handle in async thread
 * @param priority Priority of handler
 * @param ignoreCancelled Whether handler is ignored if event is cancelled
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PacketHandler(
    vararg val packets: KClass<out AbstractPacket>,
    val async: Boolean = false,
    val priority: ListenerPriority = ListenerPriority.NORMAL,
    val ignoreCancelled: Boolean = true
)