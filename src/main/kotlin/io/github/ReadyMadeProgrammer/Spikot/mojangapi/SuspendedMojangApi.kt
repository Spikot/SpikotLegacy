package io.github.ReadyMadeProgrammer.Spikot.mojangapi

import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun OfflinePlayer.getProfile(): PlayerProfile = getProfile(uniqueId)

suspend fun getProfile(name: String): PlayerProfile = suspendCoroutine { continuation ->
    namePlayerProfileCache.get(name).thenApply {
        continuation.resume(it)

    }
}

suspend fun getProfile(uuid: UUID): PlayerProfile = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.get(uuid).thenApply {
        continuation.resume(it)
    }
}

suspend fun getProfileFromPlayer(players: Collection<OfflinePlayer>): Map<UUID, PlayerProfile> = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.getAll(players.map(OfflinePlayer::getUniqueId)).thenApply {
        continuation.resume(it)
    }
}

suspend fun getProfilesFromUUID(uuids: Collection<UUID>): Map<UUID, PlayerProfile> = suspendCoroutine { continuation ->
    uuidPlayerProfileCache.getAll(uuids).thenApply {
        continuation.resume(it)
    }
}

suspend fun getProfilesFromName(names: Collection<String>): Map<String, PlayerProfile> = suspendCoroutine { continuation ->
    namePlayerProfileCache.getAll(names).thenApply {
        continuation.resume(it)
    }
}