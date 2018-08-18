package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta

class FireworkChargeItemBuilder : ItemBuilder(Material.FIREWORK_CHARGE) {
    private val fireworkMeta = meta as FireworkEffectMeta
    fun effect(build: EffectBuilder.() -> Unit) {
        val builder = EffectBuilder()
        builder.build()
        fireworkMeta.effect = builder.effect.build()
    }
}

class FireworkItemBuilder : ItemBuilder(Material.FIREWORK) {
    private val fireworkMeta = meta as FireworkMeta
    var power: Int
        get() = fireworkMeta.power
        set(value) {
            fireworkMeta.power = value
        }

    fun effects(build: FireworkBuilder.() -> Unit) {
        val builder = FireworkBuilder(fireworkMeta)
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
    val Trail: Unit
        get() {
            effect.withTrail()
        }
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