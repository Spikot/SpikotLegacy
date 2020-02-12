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

@file:Suppress("unused")

package kr.heartpattern.spikot.mojangapi

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import kr.heartpattern.spikot.serialization.jsonSerializer
import kr.heartpattern.spikot.spikot
import mu.KotlinLogging
import java.net.URL
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

private typealias Callback = (PlayerProfile) -> Unit

private val logger = KotlinLogging.logger {}

/**
 * Get cached profile. Return null if there are no cached value
 * @param uuid UUID of player
 * @return Cached PlayerProfile
 */
fun getCachedProfile(uuid: UUID): PlayerProfile? {
    return uuidPlayerProfileCache.getIfPresent(uuid)?.getNow(null)
}

@PublishedApi
internal val namePlayerProfileCache: LoadingCache<String, CompletableFuture<PlayerProfile>> = CacheBuilder
    .newBuilder()
    .expireAfterAccess(30, TimeUnit.MINUTES)
    .refreshAfterWrite(1L, TimeUnit.DAYS)
    .build(
        object : CacheLoader<String, CompletableFuture<PlayerProfile>>() {
            override fun load(key: String): CompletableFuture<PlayerProfile> {
                return spikot.future { resolve(key) }
            }
        }
    )

@PublishedApi
internal val uuidPlayerProfileCache: LoadingCache<UUID, CompletableFuture<PlayerProfile>> = CacheBuilder
    .newBuilder()
    .expireAfterAccess(30, TimeUnit.MINUTES)
    .refreshAfterWrite(1L, TimeUnit.DAYS)
    .build(
        object : CacheLoader<UUID, CompletableFuture<PlayerProfile>>() {
            override fun load(key: UUID): CompletableFuture<PlayerProfile> {
                return spikot.future { resolve(key.toString()) }
            }
        }
    )

@Suppress("SpellCheckingInspection")
suspend fun resolve(key: String): PlayerProfile {
    return withContext(Dispatchers.IO) {
        val url = URL("https://api.ashcon.app/mojang/v2/user/$key")
        val connection = url.openConnection()
        connection.setRequestProperty("User-Agent", "Spikot")
        val data = connection.getInputStream().bufferedReader().readText()
        jsonSerializer.parse(PlayerProfile.serializer(), data)
    }
}

private fun convertTime(text: String): Instant {
    val formatter = DateTimeFormatter.ISO_DATE_TIME.parse(text)
    val second = formatter.getLong(ChronoField.INSTANT_SECONDS)
    val nano = formatter.getLong(ChronoField.NANO_OF_SECOND)
    return Instant.ofEpochSecond(second, nano)
}