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

@file:UseSerializers(UUIDSerializer::class, ZonedDateTimeSerializer::class)

package kr.heartpattern.spikot.mojangapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kr.heartpattern.spikot.serialization.serializer.UUIDSerializer
import kr.heartpattern.spikot.serialization.serializer.ZonedDateTimeSerializer
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

/**
 * Represent player profile
 */
@Serializable
data class PlayerProfile(
    val uuid: UUID,
    val username: String,
    @SerialName("username_history") val usernameHistory: List<UsernameHistory>,
    val textures: Texture
)

/**
 * Represent user history
 */
@Serializable
data class UsernameHistory(
    val username: String,
    @SerialName("changed_at") val changedAt: ZonedDateTime = ZonedDateTime.ofInstant(Instant.MIN, ZoneId.systemDefault())
)

/**
 * Represent texture data
 */
@Serializable
data class Texture(
    val custom: Boolean,
    val slim: Boolean,
    val skin: MojangImage,
    val cape: MojangImage?,
    val raw: RawSkin
)

/**
 * Represent mojang image
 */
@Serializable
data class MojangImage(
    val url: String,
    val data: String
)

/**
 * Represent raw skin
 */
@Serializable
data class RawSkin(
    val value: String,
    val signature: String
)