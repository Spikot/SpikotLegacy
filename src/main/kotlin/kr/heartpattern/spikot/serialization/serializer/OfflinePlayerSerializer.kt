package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

@Serializer(forClass = OfflinePlayer::class)
object OfflinePlayerSerializer : KSerializer<OfflinePlayer> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("OfflinePlayer")

    override fun serialize(encoder: Encoder, obj: OfflinePlayer) {
        encoder.encodeString(obj.uniqueId.toString())
    }

    override fun deserialize(decoder: Decoder): OfflinePlayer {
        return Bukkit.getOfflinePlayer(UUID.fromString(decoder.decodeString()))
    }
}