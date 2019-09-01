@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.item

import io.github.ReadyMadeProgrammer.Spikot.mojangapi.PlayerProfile
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class SkullItemBuilder(itemStack: ItemStack) : ItemBuilder<SkullItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.SKULL_ITEM))

    override fun meta(build: SkullItemMetaBuilder.() -> Unit) {
        val builder = SkullItemMetaBuilder(item.itemMeta as SkullMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

class SkullItemMetaBuilder(itemMeta: SkullMeta) : ItemMetaBuilder<SkullMeta>(itemMeta) {
    var owner: OfflinePlayer
        get() = itemMeta.owningPlayer
        set(value) {
            itemMeta.owningPlayer = value
        }

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

fun createSkull(profile: PlayerProfile): ItemStack {
    return createSkull(profile.textures.skin.url.toString())
}

fun createSkull(base64: String): ItemStack {
    return createSkull0(Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"$base64\"}}}").toByteArray()))
}