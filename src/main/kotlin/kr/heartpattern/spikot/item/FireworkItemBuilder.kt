@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

class FireworkItemBuilder(itemStack: ItemStack) : ItemBuilder<FireworkItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.FIREWORK_CHARGE))

    override fun meta(build: FireworkItemMetaBuilder.() -> Unit) {
        val builder = FireworkItemMetaBuilder(item.itemMeta as FireworkMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

class FireworkItemMetaBuilder(itemMeta: FireworkMeta) : ItemMetaBuilder<FireworkMeta>(itemMeta) {
    var power: Int
        get() = itemMeta.power
        set(value) {
            itemMeta.power = value
        }

    fun effects(build: FireworkBuilder.() -> Unit) {
        val builder = FireworkBuilder(itemMeta)
        builder.build()
    }
}

@ItemDslMarker
class FireworkBuilder(private val meta: FireworkMeta) {
    fun effect(build: EffectBuilder.() -> Unit) {
        val builder = EffectBuilder()
        builder.build()
        meta.addEffect(builder.effect.build())
    }
}

@ItemDslMarker
class EffectBuilder {
    internal val effect = FireworkEffect.builder()
    var type: FireworkEffect.Type
        get() = FireworkEffect.Type.BALL
        set(value) {
            effect.with(value)
        }
    @Suppress("PropertyName")
    val Trail: Unit
        get() {
            effect.withTrail()
        }
    @Suppress("PropertyName")
    val Flicker: Unit
        get() {
            effect.withFlicker()
        }

    fun colors(build: ColorsBuilder.() -> Unit) {
        val builder = ColorsBuilder()
        builder.build()
        effect.withColor(builder.list)
    }

    fun fadeColors(build: ColorsBuilder.() -> Unit) {
        val builder = ColorsBuilder()
        builder.build()
        effect.withFade(builder.list)
    }
}

@ItemDslMarker
class ColorsBuilder {
    internal val list = mutableListOf<Color>()
    operator fun Color.unaryPlus() {
        list.add(this)
    }
}