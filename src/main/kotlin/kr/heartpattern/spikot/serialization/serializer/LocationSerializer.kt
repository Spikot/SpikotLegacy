package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import org.bukkit.Location
import org.bukkit.World

@Serializer(forClass = Location::class)
object LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Location") {
        init {
            addElement("world")
            addElement("x")
            addElement("y")
            addElement("z")
            addElement("pitch")
            addElement("yaw")
        }
    }

    override fun serialize(encoder: Encoder, obj: Location) {
        with(encoder.beginStructure(descriptor)) {
            encodeSerializableElement(descriptor, 0, WorldSerializer, obj.world)
            encodeDoubleElement(descriptor, 1, obj.x)
            encodeDoubleElement(descriptor, 2, obj.y)
            encodeDoubleElement(descriptor, 3, obj.z)
            encodeFloatElement(descriptor, 4, obj.pitch)
            encodeFloatElement(descriptor, 5, obj.yaw)
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): Location {
        with(decoder.beginStructure(descriptor)) {
            var world: World? = null
            var x: Double? = null
            var y: Double? = null
            var z: Double? = null
            var pitch: Float? = null
            var yaw: Float? = null
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> world = decodeSerializableElement(descriptor, i, WorldSerializer)
                    1 -> x = decodeDoubleElement(descriptor, i)
                    2 -> y = decodeDoubleElement(descriptor, i)
                    3 -> z = decodeDoubleElement(descriptor, i)
                    4 -> pitch = decodeFloatElement(descriptor, i)
                    5 -> yaw = decodeFloatElement(descriptor, i)
                }
            }
            return Location(
                world ?: throw MissingFieldException("world"),
                x ?: throw MissingFieldException("x"),
                y ?: throw MissingFieldException("y"),
                z ?: throw MissingFieldException("z"),
                pitch ?: 0f,
                yaw ?: 0f
            )
        }
    }
}