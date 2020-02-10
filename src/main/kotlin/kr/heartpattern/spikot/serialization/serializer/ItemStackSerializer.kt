/*
 * Copyright 2020 HeartPattern
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