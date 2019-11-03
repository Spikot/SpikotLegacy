@file:Suppress("unused")

package kr.heartpattern.spikot.mojangapi

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.salomonbrys.kotson.contains
import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser
import kr.heartpattern.spikot.logger
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.net.URL
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*

private typealias Callback = (PlayerProfile) -> Unit

@Suppress("DEPRECATION")
@Deprecated(message = "Use suspended version", replaceWith = ReplaceWith("OfflinePlayer.getProfile()"))
fun OfflinePlayer.getProfile(callback: Callback) {
    getProfile(this.uniqueId, callback)
}

@Deprecated(message = "Use suspended version", replaceWith = ReplaceWith("OfflinePlayer.getProfile(name: String))"))
fun getProfile(name: String, callback: Callback) {
    namePlayerProfileCache[name].thenApply(callback)
}

@Deprecated(message = "Use suspended version", replaceWith = ReplaceWith("OfflinePlayer.getProfile(uuid: UUID)"))
fun getProfile(uuid: UUID, callback: Callback) {
    uuidPlayerProfileCache[uuid].thenApply(callback)
}

@Deprecated(message = "Use suspended version", replaceWith = ReplaceWith("OfflinePlayer.getProfile(uuids: Collection<UUID>)"))
fun getProfile(uuid: Collection<UUID>, callback: (Map<UUID, PlayerProfile>) -> Unit) {
    uuidPlayerProfileCache.getAll(uuid).thenApply(callback)
}

@Deprecated(message = "Use suspended version", replaceWith = ReplaceWith("OfflinePlayer.getProfile(players: Collection<OfflinePlayer>)"))
fun getProfileFromPlayer(uuid: Collection<Player>, callback: (Map<UUID, PlayerProfile>) -> Unit) {
    uuidPlayerProfileCache.getAll(uuid.map { it.uniqueId }).thenApply(callback)
}

fun getCachedProfile(uuid: UUID): PlayerProfile? {
    return uuidPlayerProfileCache.getIfPresent(uuid)?.getNow(null)
}

@PublishedApi
internal val namePlayerProfileCache: AsyncLoadingCache<String, PlayerProfile> = Caffeine
    .newBuilder()
    .expireAfterAccess(Duration.ofMinutes(30L))
    .refreshAfterWrite(Duration.ofHours(1L))
    .buildAsync { key -> resolve(key) }

@PublishedApi
internal val uuidPlayerProfileCache: AsyncLoadingCache<UUID, PlayerProfile> = Caffeine
    .newBuilder()
    .expireAfterAccess(Duration.ofMinutes(30L))
    .refreshAfterWrite(Duration.ofHours(1L))
    .buildAsync { key -> resolve(key.toString()) }

@Suppress("SpellCheckingInspection")
fun resolve(key: String): PlayerProfile {
    try {
        val url = URL("https://api.ashcon.app/mojang/v2/user/$key")
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
        val textureRaw = textureJson["raw"].asJsonObject
        val textureRawValue = textureRaw["value"].asString
        val textureRawSignature = textureRaw["signature"].asString
        val texture = Texture(
            textureCustom,
            textureSlim,
            MojangImage(
                URL(textureSkinUrl),
                textureSkinData
            ),
            cape,
            RawSkin(
                textureRawValue,
                textureRawSignature
            )
        )
        return PlayerProfile(
            uuid,
            username,
            usernameHistoryList,
            texture,
            Instant.now()
        )
    } catch (e: Exception) {
        logger.error(e) { "Cannot fetch mojang api for $key" }
        return PlayerProfile(
            UUID.randomUUID(),
            key,
            emptyList(),
            Texture(
                false,
                false,
                MojangImage(
                    URL("http://textures.minecraft.net/texture/dc1c77ce8e54925ab58125446ec53b0cdd3d0ca3db273eb908d5482787ef4016"),
                    "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAABL1BMVEUBAABGOqUwKHIAr6+qfWaWX0EAaGgAf38AqKgAmZlqQDB1Ry8qHQ0mIVs/Pz9ra2uHVTuWb1soKCgAYGBWScwmGgoAzMwvHw86MYkkGAgoGwoAW1sAAABRMSUAnp4pHAwsHg6GUzQrHg2BUzkfEAsmGAsoGg0nGwstHQ4tIBCaY0QzJBFFIg6cZ0gjFwkkGAomGgwoGwsoHAsrHg4sHhEvIhEyIxBBIQw6KBRiQy9SPYl0SC+KTD2EUjGHWDqIWjmKWTucY0WcaUydak+iake0hG27iXL///8vIA1CHQo0JRI/KhVCKhJSKCZtQypvRSx6TjOAUzSDVTuPXj6QXkOWX0CcY0aaZEqfaEmcclysdlqze2K1e2etgG23gnK2iWy+iGy9i3K9jnK9jnTGloCtoI9HAAAAAXRSTlMAQObYZgAAAwBJREFUeNrtlmd7okAQxyNL2UX04O4QhAvNWNN7v/Tkeu+9ff/PcLO7bqIYA8a3/h8fdyjzY2aZh5mpqa4Mowq/6kyxq6lRZVQdBwDVos50C4Dj2BzwAPR8dEDVoTk4BgfcKgLDtp1xAMx/HIDthPYMBcR6HN/mLYQ2yDBGfo2eZzfDjXb7UeKsVO3EaLc3wqbteaIu8gDsKExmkySZffY0WplNwsimgG5dZAKiuh2uLi+Gyc8//37//fIkXFxeDe16JOoiO4JGK/Ka0bp8Jn//fH58vB41vajV8ERd5EjBW1p4eLR1drHz7XznQt46eriwBCdFXeQANOpr+8rBh68/dP3X6esDZX+t3qCbyOsiew+81vZJJy6+e7+5tzf3tlaMOyfbLS8SdZEJiONOPK8c7r58sfl4bu7Nq93DT/Mf5ztQS7QuinGuWrgPugsSxxVeS5V7XYnzuFLB+rQ+nQ3g34QBQAU0LgCDvz5WCgMASSpJBRAsdHU1TfNJUDut1YIAbC3AGCOEMbcRWxHoClDqAxQ0VdUwDsAfIbBVTO8GAJgawiig11MAqQ/AbkQ4IOAJtoq4MAMjBr0Z4KuqD9cDAn/cJggTDoCgbogADBek+r5PCHUjBEyfecOxoiimDDLBoGs/wHULdC8oAHxUwh9KAKYidoA5wJJlxbwO0LsHFAABYAaAPaDeADE5wGIAy+oBSNLAWxAAjW3iJYA+mQLM/ggEQLoCIOaFiNgwKvDjACUFKJcFoFy+A9JUTSOEBsABYLNtUDhAVmgkkEoPgDuKFVIpUWDBBQAtMtfFhLgFDrBkFkkGgEUEBCLKm8AffTL4WWY6gokmmmiiUeYFPKwr5x44QGMB8LDBYpQUcgN65wWX9gkQfOODgbkgG1C6bDQBNAmt2+rzA6RSb6fCA219FMC1c8FQQGpeGDoXDAeU+LxwCRAtLS8glQIFWBxg9s0F2QCeiskArCubOSOQUgCFA8ycgPS8oHRzp6MNTSUHoL/dsydb4wAgd8tio821gP/oPFz14izUJAAAAABJRU5ErkJggg=="),
                null,
                RawSkin(
                    "eyJ0aW1lc3RhbXAiOjE1NjYyMjMyNDM3MTgsInByb2ZpbGVJZCI6IjA2OWE3OWY0NDRlOTQ3MjZhNWJlZmNhOTBlMzhhYWY1IiwicHJvZmlsZU5hbWUiOiJOb3RjaCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkyMDA5YTQ5MjViNThmMDJjNzdkYWRjM2VjZWYwN2VhNGM3NDcyZjY0ZTBmZGMzMmNlNTUyMjQ4OTM2MjY4MCJ9fX0=",
                    "jV6J50ihAXEFjCv21+L9dYUG9BOd0DKUg9d8u1pehYY9oJs4Wm7sk9sVjZRfLYMGJt8RUT2v6lX1VLfsFsAX9DqCZQ98PdYrNe06WzUk9CEBIK0JDpWUetNH27jr/QZwslqY0k1/EyiqpWCI2UtpSXM3lfqOvGw0R1E/H4y9MIUg6vrHiHqSCruxbRcJhpdt6ZwM+xscts0hJml0KAYOXs4LWdYRJWt+eQnCm5stRjB8zY3YCpr7TfhheYhvdLnBBe/BpfVx0Mvaa6qVh3YQHY9dhM7nBUMSe+9jEFdwsm1yC3FB3Lg7F0O0lDiQ5x/V7FRxHn2Y2RnVE6YrDwzenvUiWXbPdVRmX4Hkt1RwrkbAMciaAuIETbHhQFZ2/7LzHoQVXS1qmew16Mv3rmvZ31YZnqReYo6dtJrg4i/z+mKcDerX7eyDGJhatTyAzkMa+AmER7HY1YZg9ICHksSjARG/p5BdlSEnfD7zMsRYaeXB6379KVJFQhZyo4nmEInmPIXLBWYMq5Q2lZK7jj6gjDA3O2mu34i7sLLTsHLeBEyq3KASC2aAZw+4BNhnQWfQvgvpERe91ANjszNHSeGO+8WNJG79RzY12lOxyIgqFitNxt0GZL9cNRZhpajFVu52UZSUFrh8tbvPh5q8JlAq6Hed9SzZS4tS8AoJumWyDcA="
                )),
            Instant.now()
        )
    }

}

private fun convertTime(text: String): Instant {
    val formatter = DateTimeFormatter.ISO_DATE_TIME.parse(text)
    val second = formatter.getLong(ChronoField.INSTANT_SECONDS)
    val nano = formatter.getLong(ChronoField.NANO_OF_SECOND)
    return Instant.ofEpochSecond(second, nano)
}