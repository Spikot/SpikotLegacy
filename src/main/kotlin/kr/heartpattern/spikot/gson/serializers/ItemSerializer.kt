package kr.heartpattern.spikot.gson.serializers

import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import kr.heartpattern.spikot.adapters.ItemStackAdapter
import kr.heartpattern.spikot.adapters.NBTAdapter
import kr.heartpattern.spikot.gson.GsonSerializer
import kr.heartpattern.spikot.gson.Serializer
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type
import java.util.*

@Serializer
class ItemSerializer : GsonSerializer<ItemStack> {
    override fun serialize(src: ItemStack, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return Base64.getEncoder().encodeToString(NBTAdapter.compressNBT(ItemStackAdapter.toNBTCompound(src))).toJson()
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ItemStack {
        return ItemStackAdapter.fromNBTCompound(NBTAdapter.decompressNBT(Base64.getDecoder().decode(json.string)))
    }
}