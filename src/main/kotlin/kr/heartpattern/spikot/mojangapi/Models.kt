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