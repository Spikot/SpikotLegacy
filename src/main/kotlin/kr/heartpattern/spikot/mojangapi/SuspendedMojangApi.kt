package kr.heartpattern.spikot.mojangapi

import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Get profile of offline player
 * @receiver OfflinePlayer to get profile
 * @return PlayerProfile of offline player
 */
suspend fun OfflinePlayer.getProfile(): PlayerProfile = getProfile(uniqueId)

/**
 * Get profile of name
 * @param name Name of player
 * @return PlayerProfile of name
 */
suspend fun getProfile(name: String): PlayerProfile = suspendCoroutine { continuation ->
    namePlayerProfileCache.get(name).thenApply {
        continuation.resume(it)
    }
}

/**
 * Get profile of uuid
 * @param uuid UUID of player
 * @return PlayerProfile of uuid
 */
suspend fun getProfile(uuid: UUID): PlayerProfile = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.get(uuid).thenApply {
        continuation.resume(it)
    }
}

/**
 * Get profile from collection of OfflinePlayer
 * @param players Collection of OfflinePlayer
 * @return Map of PlayerProfile
 */
suspend fun getProfileFromPlayer(players: Collection<OfflinePlayer>): Map<UUID, PlayerProfile> = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.getAll(players.map(OfflinePlayer::getUniqueId)).thenApply {
        continuation.resume(it)
    }
}

/**
 * Get profile from collection of UUID
 * @param uuids Collection of uuid
 * @return Map of PlayerProfile
 */
suspend fun getProfilesFromUUID(uuids: Collection<UUID>): Map<UUID, PlayerProfile> = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.getAll(uuids).thenApply {
        continuation.resume(it)
    }
}

/**
 * Get profile from collection of Name
 * @param names Collection of name
 * @return Map of PlayerProfile
 */
suspend fun getProfilesFromName(names: Collection<String>): Map<String, PlayerProfile> = suspendCoroutine { continuation ->
    namePlayerProfileCache.getAll(names).thenApply {
        continuation.resume(it)
    }
}