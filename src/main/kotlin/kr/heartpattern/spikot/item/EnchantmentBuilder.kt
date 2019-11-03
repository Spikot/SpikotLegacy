package kr.heartpattern.spikot.item

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

@ItemDslMarker
class EnchantmentBuilder(private val itemStack: ItemStack) {
    operator fun Enchantment.invoke(level: Int) {
        itemStack.addUnsafeEnchantment(this, level)
    }
}