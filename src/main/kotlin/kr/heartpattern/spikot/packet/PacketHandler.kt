package kr.heartpattern.spikot.packet

import com.comphenix.packetwrapper.AbstractPacket
import com.comphenix.protocol.events.ListenerPriority
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PacketHandler(
    vararg val packets: KClass<out AbstractPacket>,
    val async: Boolean = false,
    val priority: ListenerPriority = ListenerPriority.NORMAL,
    val ignoreCancelled: Boolean = true
)