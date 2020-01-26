package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kr.heartpattern.spikot.item.decodeItemStack
import kr.heartpattern.spikot.item.encode
import org.bukkit.inventory.ItemStack
import java.util.*

@Serializer(forClass = ItemStack::class)
object ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("ItemStack")

    override fun serialize(encoder: Encoder, obj: ItemStack) {
        encoder.encodeString(
            Base64.getEncoder().encodeToString(
                obj.encode()
            )
        )
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        return decodeItemStack(
            Base64.getDecoder().decode(
                decoder.decodeString()
            )
        )
    }
}