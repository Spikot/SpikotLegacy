package io.github.ReadyMadeProgrammer.Spikot.mojangapi

import java.net.URL
import java.time.Instant
import java.util.*

data class PlayerProfile(
        val uuid: UUID,
        val username: String,
        val usernameHistory: List<UsernameHistory>,
        val textures: Texture,
        val queriedAt: Instant
)

data class UsernameHistory(
        val username: String,
        val time: Instant
)

data class Texture(
        val custom: Boolean,
        val slim: Boolean,
        val skin: MojangImage,
        val cape: MojangImage?,
        val raw: RawSkin
)

data class MojangImage(
        val url: URL,
        val data: String
)

data class RawSkin(
        val data: String,
        val signature: String
)