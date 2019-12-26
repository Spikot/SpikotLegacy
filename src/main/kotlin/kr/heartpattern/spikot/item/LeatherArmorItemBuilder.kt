@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

/**
 * Configure leather item
 */
class LeatherArmorItemBuilder(itemStack: ItemStack) : ItemBuilder<LeatherArmorItemMetaBuilder>(itemStack) {
    constructor(material: Material) : this(ItemStack(material))

    override fun meta(build: LeatherArmorItemMetaBuilder.() -> Unit) {
        val builder = LeatherArmorItemMetaBuilder(item.itemMeta as LeatherArmorMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Configure leather item meta
 */
class LeatherArmorItemMetaBuilder(itemMeta: LeatherArmorMeta) : ItemMetaBuilder<LeatherArmorMeta>(itemMeta) {

    /**
     * Color of leather armor
     */
    var color: Color
        get() = itemMeta.color
        set(value) {
            itemMeta.color = value
        }
}