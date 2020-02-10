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

package kr.heartpattern.spikot.mojangapi

import kotlinx.coroutines.future.await
import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.collections.LinkedHashMap

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
    return namePlayerProfileCache.get(name).await()
}

/**
 * Get profile of uuid
 * @param uuid UUID of player
 * @return PlayerProfile of uuid
 */
suspend fun getProfile(uuid: UUID): PlayerProfile {
    return uuidPlayerProfileCache.get(uuid).await()
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
    val cached = uuidPlayerProfileCache.getAll(uuids)
    val result = LinkedHashMap<UUID, PlayerProfile>()
    for ((key, value) in cached) {
        result[key] = value.await()
    }
    return result
}

/**
 * Get profile from collection of Name
 * @param names Collection of name
 * @return Map of PlayerProfile
 */
suspend fun getProfilesFromName(names: Collection<String>): Map<String, PlayerProfile> {
    val cached = namePlayerProfileCache.getAll(names)

    val result = LinkedHashMap<String, PlayerProfile>()
    for ((key, value) in cached) {
        result[key] = value.await()
    }
    return result
}