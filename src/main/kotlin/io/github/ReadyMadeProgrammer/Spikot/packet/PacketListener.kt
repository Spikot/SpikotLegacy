package io.github.ReadyMadeProgrammer.Spikot.packet

import com.comphenix.packetwrapper.AbstractPacket
import com.comphenix.protocol.events.ListenerPriority
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PacketListener(
        val packets: Array<KClass<in AbstractPacket>>,
        val priority: ListenerPriority = ListenerPriority.NORMAL,
        val ignoreCancelled: Boolean = true
)