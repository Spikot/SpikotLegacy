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
import kotlinx.serialization.internal.StringDescriptor
import java.time.LocalDate

@Serializer(forClass = LocalDate::class)
class LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("LocalDate")

    override fun serialize(encoder: Encoder, obj: LocalDate) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}