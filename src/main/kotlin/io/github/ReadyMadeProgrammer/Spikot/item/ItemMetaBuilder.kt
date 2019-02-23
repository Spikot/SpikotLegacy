package io.github.ReadyMadeProgrammer.Spikot.item

import io.github.ReadyMadeProgrammer.Spikot.utils.InputBuilder
import io.github.ReadyMadeProgrammer.Spikot.utils.plus
import org.bukkit.ChatColor
import org.bukkit.inventory.meta.ItemMeta

@ItemDslMarker
open class ItemMetaBuilder<T : ItemMeta>(internal val itemMeta: T) {
    val UnBreakable: Unit
        get() {
            itemMeta.isUnbreakable = true
        }

    var displayName: String
        get() = itemMeta.displayName
        set(value) {
            itemMeta.displayName = ChatColor.RESET + value
        }
    var localizedName: String
        get() = itemMeta.localizedName
        set(value) {
            itemMeta.displayName = value
        }

    fun lore(build: InputBuilder<String>.() -> Unit) {
        val builder = InputBuilder<String>()
        builder.build()
        itemMeta.lore = builder.data.map { ChatColor.RESET + it }
    }

    fun itemFlag(build: ItemFlagBuilder.() -> Unit) {
        val builder = ItemFlagBuilder(itemMeta)
        builder.build()
    }
}