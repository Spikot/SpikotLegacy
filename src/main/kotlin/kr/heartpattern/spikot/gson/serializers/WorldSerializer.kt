package kr.heartpattern.spikot.gson.serializers

import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import kr.heartpattern.spikot.gson.GsonSerializer
import kr.heartpattern.spikot.gson.Serializer
import org.bukkit.Bukkit
import org.bukkit.World
import java.lang.reflect.Type
import java.util.*

@Serializer
class WorldSerializer : GsonSerializer<World> {
    override fun serialize(src: World, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src.uid.toString().toJson()
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): World {
        return Bukkit.getWorld(UUID.fromString(json.string))
    }
}