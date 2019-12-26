@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

/**
 * Configure banner item meta
 */
class BannerItemMetaBuilder(itemMeta: BannerMeta) : ItemMetaBuilder<BannerMeta>(itemMeta) {
    /**
     * Add new pattern to this banner
     */
    operator fun PatternType.invoke(color: DyeColor) {
        itemMeta.addPattern(Pattern(color, this))
    }
}

/**
 * Configure banner item
 */
class BannerItemBuilder(item: ItemStack) : ItemBuilder<BannerItemMetaBuilder>(item) {
    constructor() : this(ItemStack(Material.BANNER))

    override fun meta(build: BannerItemMetaBuilder.() -> Unit) {
        val builder = BannerItemMetaBuilder(item.itemMeta as BannerMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}