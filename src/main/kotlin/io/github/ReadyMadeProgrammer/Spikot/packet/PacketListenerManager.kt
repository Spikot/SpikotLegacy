package io.github.ReadyMadeProgrammer.Spikot.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.github.ReadyMadeProgrammer.Spikot.module.AbstractModule
import io.github.ReadyMadeProgrammer.Spikot.module.LoadOrder
import io.github.ReadyMadeProgrammer.Spikot.module.Module
import io.github.ReadyMadeProgrammer.Spikot.module.ModuleManager

@Module(LoadOrder.API)
object PacketListenerManager : AbstractModule() {
    override fun onEnable() {
        val manager = ProtocolLibrary.getProtocolManager()
        ModuleManager.instances.forEach { (holder, instance) ->
            for (method in instance.javaClass.declaredMethods) {
                val annotation = method.getDeclaredAnnotation(PacketHandler::class.java)!!
                manager.addPacketListener(object : PacketAdapter(
                        holder.plugin,
                        annotation.priority,
                        annotation.packets.map {
                            it.java.getDeclaredField("TYPE").get(null) as PacketType
                        }
                ) {
                    override fun onPacketReceiving(event: PacketEvent) {
                        if (!event.isCancelled || !annotation.ignoreCancelled) {
                            method.invoke(instance, event)
                        }
                    }

                    override fun onPacketSending(event: PacketEvent) {
                        if (!event.isCancelled || !annotation.ignoreCancelled) {
                            method.invoke(instance, event)
                        }
                    }
                })
            }
        }
    }
}