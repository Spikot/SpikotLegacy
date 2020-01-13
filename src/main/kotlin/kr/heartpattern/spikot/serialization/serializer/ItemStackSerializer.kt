package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kr.heartpattern.spikot.adapters.ItemStackAdapter
import kr.heartpattern.spikot.adapters.NBTAdapter
import org.bukkit.inventory.ItemStack
import java.util.*

@Serializer(forClass = ItemStack::class)
object ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("ItemStack")

    override fun serialize(encoder: Encoder, obj: ItemStack) {
        encoder.encodeString(
            Base64.getEncoder().encodeToString(
                NBTAdapter.compressNBT(
                    ItemStackAdapter.toNBTCompound(obj)
                )
            )
        )
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        return ItemStackAdapter.fromNBTCompound(
            NBTAdapter.decompressNBT(
                Base64.getDecoder().decode(
                    decoder.decodeString()
                )
            )
        )
    }
}