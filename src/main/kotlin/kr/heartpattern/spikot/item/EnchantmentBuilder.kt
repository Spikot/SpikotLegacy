package kr.heartpattern.spikot.item

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

/**
 * Add enchantment to item
 */
@ItemDslMarker
class EnchantmentBuilder(private val itemStack: ItemStack) {
    /**
     * Add new enchantment
     * @param level Enchantment level
     */
    operator fun Enchantment.invoke(level: Int) {
        itemStack.addUnsafeEnchantment(this, level)
    }
}