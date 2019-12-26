package kr.heartpattern.spikot.item

import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

/**
 * Configure flag of ItemStack
 */
@ItemDslMarker
class ItemFlagBuilder(private val itemMeta: ItemMeta) {
    val HIDE_ENCHANT: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    val HIDE_ATTRIBUTES: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    val HIDE_UNBREAKABLE: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
    val HIDE_DESTROYS: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS)
        }
    val HIDE_PLACED_ON: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON)
        }
    val HIDE_POTION_EFFECTS: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        }
}