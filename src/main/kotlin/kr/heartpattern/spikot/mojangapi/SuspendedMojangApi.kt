package kr.heartpattern.spikot.mojangapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.collections.HashMap

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
suspend fun getProfile(name: String): PlayerProfile {
    val cached = namePlayerProfileCache.getIfPresent(name)
    return cached ?: withContext(Dispatchers.IO) {
        val fetched = resolve(name)
        namePlayerProfileCache.put(name, fetched)
        fetched
    }
}

/**
 * Get profile of uuid
 * @param uuid UUID of player
 * @return PlayerProfile of uuid
 */
suspend fun getProfile(uuid: UUID): PlayerProfile {
    val cached = uuidPlayerProfileCache.getIfPresent(uuid)
    return cached ?: withContext(Dispatchers.IO) {
        val fetched = resolve(uuid.toString())
        uuidPlayerProfileCache.put(uuid, fetched)
        fetched
    }
}

/**
 * Get profile from collection of OfflinePlayer
 * @param players Collection of OfflinePlayer
 * @return Map of PlayerProfile
 */
suspend fun getProfileFromPlayer(players: Collection<OfflinePlayer>): Map<UUID, PlayerProfile> {
    return getProfilesFromUUID(players.map { it.uniqueId })
}

/**
 * Get profile from collection of UUID
 * @param uuids Collection of uuid
 * @return Map of PlayerProfile
 */
suspend fun getProfilesFromUUID(uuids: Collection<UUID>): Map<UUID, PlayerProfile> {
    val cached = uuidPlayerProfileCache.getAllPresent(uuids)
    return if (cached.size == uuids.size) {
        cached
    } else {
        withContext(Dispatchers.IO) {
            val loadedMap = HashMap<UUID, PlayerProfile>(cached)
            for (uuid in uuids - cached.keys) {
                val fetched = resolve(uuid.toString())
                uuidPlayerProfileCache.put(uuid, fetched)
                loadedMap[uuid] = fetched
            }
            loadedMap
        }
    }
}

/**
 * Get profile from collection of Name
 * @param names Collection of name
 * @return Map of PlayerProfile
 */
suspend fun getProfilesFromName(names: Collection<String>): Map<String, PlayerProfile> {
    val cached = namePlayerProfileCache.getAllPresent(names)
    return if (cached.size == names.size) {
        cached
    } else {
        withContext(Dispatchers.IO) {
            val loadedMap = HashMap<String, PlayerProfile>(cached)
            for (name in names - cached.keys) {
                val fetched = resolve(name)
                namePlayerProfileCache.put(name, fetched)
                loadedMap[name] = fetched
            }
            loadedMap
        }
    }
}