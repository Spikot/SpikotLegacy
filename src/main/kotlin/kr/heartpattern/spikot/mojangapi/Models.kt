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

package kr.heartpattern.spikot.mojangapi

import java.net.URL
import java.time.Instant
import java.util.*

/**
 * Represent player profile
 */
data class PlayerProfile(
    val uuid: UUID,
    val username: String,
    val usernameHistory: List<UsernameHistory>,
    val textures: Texture,
    val queriedAt: Instant
)

/**
 * Represent user history
 */
data class UsernameHistory(
    val username: String,
    val time: Instant
)

/**
 * Represent texture data
 */
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
data class MojangImage(
    val url: URL,
    val data: String
)

/**
 * Represent raw skin
 */
data class RawSkin(
    val data: String,
    val signature: String
)