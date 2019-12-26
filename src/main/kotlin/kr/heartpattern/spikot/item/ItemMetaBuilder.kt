package kr.heartpattern.spikot.item

import kr.heartpattern.spikot.utils.ListBuilder
import kr.heartpattern.spikot.utils.plus
import org.bukkit.ChatColor
import org.bukkit.inventory.meta.ItemMeta

/**
 * Configure ItemMeta
 */
@ItemDslMarker
open class ItemMetaBuilder<T : ItemMeta>(internal val itemMeta: T) {

    /**
     * Add unbreakable attribute to item
     */
    val UnBreakable: Unit
        get() {
            itemMeta.isUnbreakable = true
        }

    /**
     * Display name of item
     */
    var displayName: String
        get() = itemMeta.displayName
        set(value) {
            itemMeta.displayName = ChatColor.RESET + value
        }

    /**
     * Localized name of item
     */
    var localizedName: String
        get() = itemMeta.localizedName
        set(value) {
            itemMeta.displayName = value
        }

    /**
     * Configure lore of item
     * @param build lambda that configure lore
     */
    fun lore(build: ListBuilder<String>.() -> Unit) {
        val builder = ListBuilder<String>()
        builder.build()
        itemMeta.lore = builder.data.map { ChatColor.RESET + it }
    }

    /**
     * Configure flag of item,
     * @param build lambda that configure lore
     */
    fun itemFlag(build: ItemFlagBuilder.() -> Unit) {
        val builder = ItemFlagBuilder(itemMeta)
        builder.build()
    }
}