@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

/**
 * Configure firework item
 */
class FireworkItemBuilder(itemStack: ItemStack) : ItemBuilder<FireworkItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.FIREWORK_CHARGE))

    override fun meta(build: FireworkItemMetaBuilder.() -> Unit) {
        val builder = FireworkItemMetaBuilder(item.itemMeta as FireworkMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Configure firework item meta
 */
class FireworkItemMetaBuilder(itemMeta: FireworkMeta) : ItemMetaBuilder<FireworkMeta>(itemMeta) {
    /**
     * Power of firework
     */
    var power: Int
        get() = itemMeta.power
        set(value) {
            itemMeta.power = value
        }

    /**
     * Configure effect of firework
     * @param build lambda that configure firework effect
     */
    fun effects(build: FireworkBuilder.() -> Unit) {
        val builder = FireworkBuilder(itemMeta)
        builder.build()
    }
}

/**
 * Configure firework effect
 */
@ItemDslMarker
class FireworkBuilder(private val meta: FireworkMeta) {
    /**
     * Add new effect to firework
     * @param build lambda that configure effect
     */
    fun effect(build: EffectBuilder.() -> Unit) {
        val builder = EffectBuilder()
        builder.build()
        meta.addEffect(builder.effect.build())
    }
}

/**
 * Configure effect
 */
@ItemDslMarker
class EffectBuilder {
    internal val effect = FireworkEffect.builder()

    /**
     * FireworkEffect type
     */
    var type: FireworkEffect.Type
        get() = FireworkEffect.Type.BALL
        set(value) {
            effect.with(value)
        }

    /**
     * Set firework trail
     */
    @Suppress("PropertyName")
    val Trail: Unit
        get() {
            effect.withTrail()
        }

    /**
     * Set firework flicker
     */
    @Suppress("PropertyName")
    val Flicker: Unit
        get() {
            effect.withFlicker()
        }

    /**
     * Set effect colors
     * @param build lambda that configure color
     */
    fun colors(build: ColorsBuilder.() -> Unit) {
        val builder = ColorsBuilder()
        builder.build()
        effect.withColor(builder.list)
    }

    /**
     * Set effect fade colors
     * @param build lambda that configure color
     */
    fun fadeColors(build: ColorsBuilder.() -> Unit) {
        val builder = ColorsBuilder()
        builder.build()
        effect.withFade(builder.list)
    }
}

/**
 * Configure color
 */
@ItemDslMarker
class ColorsBuilder {
    internal val list = mutableListOf<Color>()
    /**
     * Add new color
     * @receiver Color to add
     */
    operator fun Color.unaryPlus() {
        list.add(this)
    }
}