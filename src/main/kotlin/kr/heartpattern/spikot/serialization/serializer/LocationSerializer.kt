/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import org.bukkit.Location
import org.bukkit.World

@Serializer(forClass = Location::class)
object LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor = SerialDescriptor("Location") {
        element("world", String.serializer().descriptor)
        element("x", Double.serializer().descriptor)
        element("y", Double.serializer().descriptor)
        element("z", Double.serializer().descriptor)
        element("pitch", Double.serializer().descriptor)
        element("yaw", Double.serializer().descriptor)
    }

    override fun serialize(encoder: Encoder, value: Location) {
        with(encoder.beginStructure(descriptor)) {
            encodeSerializableElement(descriptor, 0, WorldSerializer, value.world)
            encodeDoubleElement(descriptor, 1, value.x)
            encodeDoubleElement(descriptor, 2, value.y)
            encodeDoubleElement(descriptor, 3, value.z)
            encodeFloatElement(descriptor, 4, value.pitch)
            encodeFloatElement(descriptor, 5, value.yaw)
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
            endStructure(descriptor)
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