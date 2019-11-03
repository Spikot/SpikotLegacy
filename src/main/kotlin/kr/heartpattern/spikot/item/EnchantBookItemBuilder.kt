package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class EnchantBookItemBuilder(itemStack: ItemStack) : ItemBuilder<EnchantBookItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.ENCHANTED_BOOK))

    override fun meta(build: EnchantBookItemMetaBuilder.() -> Unit) {
        val builder = EnchantBookItemMetaBuilder(item.itemMeta as EnchantmentStorageMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

@ItemDslMarker
class EnchantBookItemMetaBuilder(itemMeta: EnchantmentStorageMeta) : ItemMetaBuilder<EnchantmentStorageMeta>(itemMeta) {
    operator fun Enchantment.invoke(level: Int) {
        itemMeta.addEnchant(this, level, true)
    }
}