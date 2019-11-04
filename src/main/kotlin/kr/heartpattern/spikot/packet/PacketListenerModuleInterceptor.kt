package kr.heartpattern.spikot.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerOptions
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.module.IModuleInterceptor
import kr.heartpattern.spikot.module.ModuleHandler
import kr.heartpattern.spikot.module.ModuleInterceptor
import java.util.*

/**
 * Register packet handler in module
 */
@ModuleInterceptor
private object PacketListenerModuleInterceptor : IModuleInterceptor {
    private object RegisteredPacketListener : MutableProperty<List<PacketListener>>

    override fun onEnable(handler: ModuleHandler) {
        val plugin = handler.owner
        val module = handler.module!!

        val manager = ProtocolLibrary.getProtocolManager()
        val registeredListener = LinkedList<PacketListener>()

        for (method in module::class.java.declaredMethods) {
            val annotation = method.getDeclaredAnnotation(PacketHandler::class.java) ?: continue
            val options = if (annotation.async) arrayOf(ListenerOptions.ASYNC) else arrayOf()
            val packetAdapter = object : PacketAdapter(
                plugin,
                annotation.priority,
                annotation.packets.map {
                    it.java.getDeclaredField("TYPE").get(null) as PacketType
                },
                *options
            ) {
                override fun onPacketReceiving(event: PacketEvent) {
                    if (!event.isCancelled || !annotation.ignoreCancelled) {
                        method.invoke(module, event)
                    }
                }

                override fun onPacketSending(event: PacketEvent) {
                    if (!event.isCancelled || !annotation.ignoreCancelled) {
                        method.invoke(module, event)
                    }
                }
            }
            registeredListener.add(packetAdapter)
            manager.addPacketListener(packetAdapter)
        }
        handler.context[RegisteredPacketListener] = registeredListener
    }

    override fun onDisable(handler: ModuleHandler) {
        val manager = ProtocolLibrary.getProtocolManager()
        handler.context[RegisteredPacketListener]?.forEach { listener ->
            manager.removePacketListener(listener)
        }
    }

    override fun onError(handler: ModuleHandler, state: IModule.State, throwable: Throwable) {
        val manager = ProtocolLibrary.getProtocolManager()
        handler.context[RegisteredPacketListener]?.forEach { listener ->
            manager.removePacketListener(listener)
        }
    }
}