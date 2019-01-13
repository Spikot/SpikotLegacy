@file:Suppress("unused")
package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

class BannerItemMetaBuilder(itemMeta: BannerMeta) : ItemMetaBuilder<BannerMeta>(itemMeta) {
    operator fun PatternType.invoke(color: DyeColor) {
        itemMeta.addPattern(Pattern(color, this))
    }
}

class BannerItemBuilder(item: ItemStack) : ItemBuilder<BannerItemMetaBuilder>(item) {
    constructor() : this(ItemStack(Material.BANNER))

    override fun meta(build: BannerItemMetaBuilder.() -> Unit) {
        val builder = BannerItemMetaBuilder(item.itemMeta as BannerMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}