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