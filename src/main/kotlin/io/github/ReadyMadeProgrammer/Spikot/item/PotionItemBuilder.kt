@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.item

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class PotionItemBuilder(itemStack: ItemStack) : ItemBuilder<PotionItemMetaBuilder>(itemStack) {
    constructor(material: Material) : this(ItemStack(material))

    override fun meta(build: PotionItemMetaBuilder.() -> Unit) {
        val builder = PotionItemMetaBuilder(item.itemMeta as PotionMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

class PotionItemMetaBuilder(itemMeta: PotionMeta) : ItemMetaBuilder<PotionMeta>(itemMeta) {
    var color: Color
        get() = itemMeta.color
        set(value) {
            itemMeta.color = value
        }

    fun potionData(build: PotionDataBuilder.() -> Unit) {
        val builder = PotionDataBuilder()
        builder.build()
        itemMeta.basePotionData = builder.toPotionData()
    }

    fun customEffects(build: PotionEffectBuilder.() -> Unit) {
        val builder = PotionEffectBuilder()
        builder.build()
        itemMeta.addCustomEffect(builder.toPotionEffect(), true)
    }
}

@ItemDslMarker
@Suppress("PropertyName")
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

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
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