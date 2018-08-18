package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class EnchantBookItemBuilder : ItemBuilder(Material.ENCHANTED_BOOK) {
    val enchantMeta = item.itemMeta as EnchantmentStorageMeta
    fun enchantmentStorages(build: EnchantmentStorageBuilder.() -> Unit) {
        val builder = EnchantmentStorageBuilder(enchantMeta)
        builder.build()
    }
}

@ItemDslMarker
class EnchantmentStorageBuilder(private val meta: EnchantmentStorageMeta) {
    operator fun Enchantment.invoke(level: Int) {
        meta.addStoredEnchant(this, level, true)
    }
}