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

package kr.heartpattern.spikot.item

import kr.heartpattern.spikot.mojangapi.PlayerProfile
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Configure skull item
 */
class SkullItemBuilder(itemStack: ItemStack) : ItemBuilder<SkullItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.SKULL_ITEM))

    override fun meta(build: SkullItemMetaBuilder.() -> Unit) {
        val builder = SkullItemMetaBuilder(item.itemMeta as SkullMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Configure skull item meta
 */
class SkullItemMetaBuilder(itemMeta: SkullMeta) : ItemMetaBuilder<SkullMeta>(itemMeta) {
    /**
     * Owner of skull
     */
    var owner: OfflinePlayer
        get() = itemMeta.owningPlayer
        set(value) {
            itemMeta.owningPlayer = value
        }

    /**
     * Owing player of skull
     */
    var owingPlayer: OfflinePlayer
        get() = itemMeta.owningPlayer
        set(value) {
            itemMeta.owningPlayer = value
        }
}

//Temporal Api
private fun createSkull0(base64: String): ItemStack {
    val hashed = UUID(base64.hashCode().toLong(), base64.hashCode().toLong())
    val itemStack = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
    @Suppress("DEPRECATION")
    return Bukkit.getUnsafe().modifyItemStack(itemStack,
        "{SkullOwner:{Id:\"$hashed\",Properties:{textures:[{Value:\"$base64\"}]}}}")
}

/**
 * Create skull from player profile
 * @param profile Profile of skull skin
 * @return Created skull item
 */
fun createSkull(profile: PlayerProfile): ItemStack {
    return createSkull(profile.textures.skin.url.toString())
}

/**
 * Create skull from skull texture
 * @param base64 Base64 encoded skull texture
 * @return Created skull item
 */
fun createSkull(base64: String): ItemStack {
    return createSkull0(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"$base64\"}}}").toByteArray()))
}