package kr.heartpattern.spikot.gson.serializers

import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import kr.heartpattern.spikot.gson.GsonSerializer
import kr.heartpattern.spikot.gson.Serializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.lang.reflect.Type
import java.util.*

@Serializer
class OfflinePlayerSerializer: GsonSerializer<OfflinePlayer>{
    override fun serialize(src: OfflinePlayer, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src.uniqueId.toString().toJson()
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): OfflinePlayer {
        return Bukkit.getOfflinePlayer(UUID.fromString(json.string))
    }
}