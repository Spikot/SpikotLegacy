@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.mojangapi

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.salomonbrys.kotson.contains
import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser
import org.bukkit.OfflinePlayer
import java.net.URL
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*

private typealias Callback = (PlayerProfile) -> Unit

fun OfflinePlayer.getProfile(callback: Callback) {
    getProfile(this.uniqueId, callback)
}

fun getProfile(name: String, callback: Callback) {
    namePlayerProfileCache[name].thenApply(callback)
}

fun getProfile(uuid: UUID, callback: Callback) {
    uuidPlayerProfileCache[uuid].thenApply(callback)
}

fun getProfile(uuid: Collection<UUID>, callback: (Map<UUID, PlayerProfile>) -> Unit) {
    uuidPlayerProfileCache.getAll(uuid).thenApply(callback)
}

internal val namePlayerProfileCache: AsyncLoadingCache<String, PlayerProfile> = Caffeine
        .newBuilder()
        .expireAfterAccess(Duration.ofMinutes(30L))
        .refreshAfterWrite(Duration.ofHours(1L))
        .buildAsync { key -> resolve(key) }

internal val uuidPlayerProfileCache: AsyncLoadingCache<UUID, PlayerProfile> = Caffeine
        .newBuilder()
        .expireAfterAccess(Duration.ofMinutes(30L))
        .refreshAfterWrite(Duration.ofHours(1L))
        .buildAsync { key -> resolve(key.toString()) }

@Suppress("SpellCheckingInspection")
fun resolve(key: String): PlayerProfile? {
    try {
        val url = URL("https://api.ashcon.app/mojang/v1/user/$key")
        val connection = url.openConnection()
        connection.setRequestProperty("User-Agent", "Spikot")
        val data = connection.getInputStream().bufferedReader().readText()
        val json = JsonParser().parse(data).asJsonObject
        val uuid = UUID.fromString(json["uuid"].asString)
        val username = json["username"].asString
        val usernameHistoryJson = json["username_history"].asJsonArray
        val usernameHistoryList = ArrayList<UsernameHistory>()
        usernameHistoryJson.forEach { element ->
            val historyUsername = element["username"].asString
            val historyChangedAt = if ("changed_at" in element.asJsonObject) {
                convertTime(element["changed_at"].asString)
            } else {
                Instant.MIN
            }
            usernameHistoryList.add(UsernameHistory(historyUsername, historyChangedAt))
        }
        val textureJson = json["textures"].asJsonObject
        val textureCustom = textureJson["custom"].asBoolean
        val textureSlim = textureJson["slim"].asBoolean
        val textureSkinJson = textureJson["skin"].asJsonObject
        val textureSkinUrl = textureSkinJson["url"].asString
        val textureSkinData = textureSkinJson["data"].asString
        val cape = if ("cape" in textureJson) {
            val textureCapeJson = textureJson["cape"].asJsonObject
            val textureCapeUrl = textureCapeJson["url"].asString
            val textureCapeData = textureCapeJson["data"].asString
            MojangImage(URL(textureCapeUrl), textureCapeData)
        } else {
            null
        }
        val texture = Texture(textureCustom, textureSlim, MojangImage(URL(textureSkinUrl), textureSkinData), cape)
        val cachedAt = convertTime(json["cached_at"].asString)
        return PlayerProfile(uuid, username, usernameHistoryList, texture, cachedAt, Instant.now())
    } catch (e: Exception) {
        return PlayerProfile(UUID.randomUUID(), key, emptyList(), Texture(false, false, MojangImage(URL("http://textures.minecraft.net/texture/dc1c77ce8e54925ab58125446ec53b0cdd3d0ca3db273eb908d5482787ef4016"), "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAABL1BMVEUBAABGOqUwKHIAr6+qfWaWX0EAaGgAf38AqKgAmZlqQDB1Ry8qHQ0mIVs/Pz9ra2uHVTuWb1soKCgAYGBWScwmGgoAzMwvHw86MYkkGAgoGwoAW1sAAABRMSUAnp4pHAwsHg6GUzQrHg2BUzkfEAsmGAsoGg0nGwstHQ4tIBCaY0QzJBFFIg6cZ0gjFwkkGAomGgwoGwsoHAsrHg4sHhEvIhEyIxBBIQw6KBRiQy9SPYl0SC+KTD2EUjGHWDqIWjmKWTucY0WcaUydak+iake0hG27iXL///8vIA1CHQo0JRI/KhVCKhJSKCZtQypvRSx6TjOAUzSDVTuPXj6QXkOWX0CcY0aaZEqfaEmcclysdlqze2K1e2etgG23gnK2iWy+iGy9i3K9jnK9jnTGloCtoI9HAAAAAXRSTlMAQObYZgAAAwBJREFUeNrtlmd7okAQxyNL2UX04O4QhAvNWNN7v/Tkeu+9ff/PcLO7bqIYA8a3/h8fdyjzY2aZh5mpqa4Mowq/6kyxq6lRZVQdBwDVos50C4Dj2BzwAPR8dEDVoTk4BgfcKgLDtp1xAMx/HIDthPYMBcR6HN/mLYQ2yDBGfo2eZzfDjXb7UeKsVO3EaLc3wqbteaIu8gDsKExmkySZffY0WplNwsimgG5dZAKiuh2uLi+Gyc8//37//fIkXFxeDe16JOoiO4JGK/Ka0bp8Jn//fH58vB41vajV8ERd5EjBW1p4eLR1drHz7XznQt46eriwBCdFXeQANOpr+8rBh68/dP3X6esDZX+t3qCbyOsiew+81vZJJy6+e7+5tzf3tlaMOyfbLS8SdZEJiONOPK8c7r58sfl4bu7Nq93DT/Mf5ztQS7QuinGuWrgPugsSxxVeS5V7XYnzuFLB+rQ+nQ3g34QBQAU0LgCDvz5WCgMASSpJBRAsdHU1TfNJUDut1YIAbC3AGCOEMbcRWxHoClDqAxQ0VdUwDsAfIbBVTO8GAJgawiig11MAqQ/AbkQ4IOAJtoq4MAMjBr0Z4KuqD9cDAn/cJggTDoCgbogADBek+r5PCHUjBEyfecOxoiimDDLBoGs/wHULdC8oAHxUwh9KAKYidoA5wJJlxbwO0LsHFAABYAaAPaDeADE5wGIAy+oBSNLAWxAAjW3iJYA+mQLM/ggEQLoCIOaFiNgwKvDjACUFKJcFoFy+A9JUTSOEBsABYLNtUDhAVmgkkEoPgDuKFVIpUWDBBQAtMtfFhLgFDrBkFkkGgEUEBCLKm8AffTL4WWY6gokmmmiiUeYFPKwr5x44QGMB8LDBYpQUcgN65wWX9gkQfOODgbkgG1C6bDQBNAmt2+rzA6RSb6fCA219FMC1c8FQQGpeGDoXDAeU+LxwCRAtLS8glQIFWBxg9s0F2QCeiskArCubOSOQUgCFA8ycgPS8oHRzp6MNTSUHoL/dsydb4wAgd8tio821gP/oPFz14izUJAAAAABJRU5ErkJggg=="), null), Instant.now(), Instant.now())
    }

}

private fun convertTime(text: String): Instant {
    val formatter = DateTimeFormatter.ISO_DATE_TIME.parse(text)
    val second = formatter.getLong(ChronoField.INSTANT_SECONDS)
    val nano = formatter.getLong(ChronoField.NANO_OF_SECOND)
    return Instant.ofEpochSecond(second, nano)
}