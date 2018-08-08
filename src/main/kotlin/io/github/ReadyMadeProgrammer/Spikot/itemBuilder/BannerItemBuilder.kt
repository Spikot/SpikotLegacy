package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

class BannerItemBuilder : ItemBuilder(Material.BANNER) {
    fun pattern(build: PatternBuilder.() -> Unit) {
        val builder = PatternBuilder(item)
        builder.build()
    }
}

@ItemDslMarker
class PatternBuilder(itemStack: ItemStack) {
    private val itemMeta = itemStack.itemMeta as BannerMeta
    var baseColor: DyeColor
        get() = itemMeta.baseColor
        set(value) {
            itemMeta.baseColor = value
        }

    operator fun PatternType.invoke(color: DyeColor) {
        itemMeta.addPattern(Pattern(color, this))
    }
}