package io.github.ReadyMadeProgrammer.Spikot.mojangapi

import org.bukkit.OfflinePlayer
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@PublishedApi
internal suspend inline fun <T> callbackHelper(crossinline func: ((T) -> Any?) -> Any?): T = suspendCoroutine { continuation ->
    func(continuation::resume)
}

suspend inline fun OfflinePlayer.getProfile(): PlayerProfile = getProfile(uniqueId)

suspend inline fun getProfile(name: String): PlayerProfile = callbackHelper(namePlayerProfileCache.get(name)::thenApply)

suspend inline fun getProfile(uuid: UUID): PlayerProfile = callbackHelper(uuidPlayerProfileCache.get(uuid)::thenApply)

suspend inline fun getProfileFromPlayer(players: Collection<OfflinePlayer>): Map<UUID, PlayerProfile> = callbackHelper(uuidPlayerProfileCache.getAll(players.map(OfflinePlayer::getUniqueId))::thenApply)

suspend inline fun getProfilesFromUUID(uuids: Collection<UUID>): Map<UUID, PlayerProfile> = callbackHelper(uuidPlayerProfileCache.getAll(uuids)::thenApply)

suspend inline fun getProfilesFromName(names: Collection<String>): Map<String, PlayerProfile> = callbackHelper(namePlayerProfileCache.getAll(names)::thenApply)