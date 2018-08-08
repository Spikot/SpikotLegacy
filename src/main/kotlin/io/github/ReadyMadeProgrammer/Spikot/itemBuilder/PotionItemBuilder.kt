package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class PotionItemBuilder(material: Material) : ItemBuilder(material) {
    private val potionMeta = meta as PotionMeta
    var color: Color
        get() = potionMeta.color
        set(value) {
            potionMeta.color = value
        }

    fun potionData(build: PotionDataBuilder.() -> Unit) {
        val builder = PotionDataBuilder()
        builder.build()
        potionMeta.basePotionData = builder.toPotionData()
    }

    fun customEffects(build: PotionEffectBuilder.() -> Unit) {
        val builder = PotionEffectBuilder()
        builder.build()
        potionMeta.addCustomEffect(builder.toPotionEffect(), true)
    }
}

@ItemDslMarker
class PotionDataBuilder {
    var type: PotionType? = null
    private var isExtended: Boolean = false
    private var isUpgraded: Boolean = false
    val Extended: Unit
        get() {
            isExtended = true
        }
    val Upgraded: Unit
        get() {
            isUpgraded = true
        }

    fun toPotionData(): PotionData = PotionData(type, isExtended, isUpgraded)
}

@ItemDslMarker
class PotionEffectBuilder {
    var type: PotionEffectType? = null
    var duration: Int = 0
    var amplifier: Int = 1
    private var isAmbient: Boolean = false
    private var isParticles: Boolean = false
    var color: Color? = null
    val Ambient: Unit
        get() {
            isAmbient = true
        }
    val Particles: Unit
        get() {
            isParticles = true
        }

    fun toPotionEffect(): PotionEffect = if (color == null) PotionEffect(type, duration, amplifier, isAmbient, isParticles)
    else PotionEffect(type, duration, amplifier, isAmbient, isParticles, color)
}