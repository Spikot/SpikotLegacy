package kr.heartpattern.spikot.gson.serializers

import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import kr.heartpattern.spikot.gson.GsonSerializer
import kr.heartpattern.spikot.gson.Serializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Type
import java.util.*

@Serializer
class PlayerSerializer : GsonSerializer<Player> {
    override fun serialize(src: Player, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src.uniqueId.toString().toJson()
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Player {
        return Bukkit.getPlayer(UUID.fromString(json.string))
    }
}