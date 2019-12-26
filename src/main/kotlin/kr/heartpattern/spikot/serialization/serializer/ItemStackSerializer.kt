package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.HexConverter
import kotlinx.serialization.internal.StringDescriptor
import kr.heartpattern.spikot.adapters.ItemStackAdapter
import kr.heartpattern.spikot.adapters.NBTAdapter
import org.bukkit.inventory.ItemStack

@Serializer(forClass = ItemStack::class)
object ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("ItemStack")

    override fun serialize(encoder: Encoder, obj: ItemStack) {
        encoder.encodeString(
            HexConverter.printHexBinary(
                NBTAdapter.compressNBT(
                    ItemStackAdapter.toNBTCompound(obj)
                )
            )
        )
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        return ItemStackAdapter.fromNBTCompound(
            NBTAdapter.decompressNBT(
                HexConverter.parseHexBinary(
                    decoder.decodeString()
                )
            )
        )
    }
}