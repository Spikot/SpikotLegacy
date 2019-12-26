package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.*

@Serializer(forClass = World::class)
object WorldSerializer : KSerializer<World> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("World")

    override fun serialize(encoder: Encoder, obj: World) {
        encoder.encodeString(obj.uid.toString())
    }

    override fun deserialize(decoder: Decoder): World {
        return Bukkit.getWorld(UUID.fromString(decoder.decodeString()))
    }
}