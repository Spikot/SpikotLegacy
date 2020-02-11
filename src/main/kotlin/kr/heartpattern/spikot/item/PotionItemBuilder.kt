/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:Suppress("unused")

package kr.heartpattern.spikot.item

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

/**
 * Configure potion item
 */
class PotionItemBuilder(itemStack: ItemStack) : ItemBuilder<PotionItemMetaBuilder>(itemStack) {
    constructor(material: Material) : this(ItemStack(material))

    override fun meta(build: PotionItemMetaBuilder.() -> Unit) {
        val builder = PotionItemMetaBuilder(item.itemMeta as PotionMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Configure potion item meta
 */
class PotionItemMetaBuilder(itemMeta: PotionMeta) : ItemMetaBuilder<PotionMeta>(itemMeta) {
    /**
     * Color of potion
     */
    var color: Color
        get() = itemMeta.color
        set(value) {
            itemMeta.color = value
        }

    /**
     * Configure potion data
     * @param build lambda that configure potion data
     */
    fun potionData(build: PotionDataBuilder.() -> Unit) {
        val builder = PotionDataBuilder()
        builder.build()
        itemMeta.basePotionData = builder.toPotionData()
    }

    /**
     * Configure custom effect
     * @param build lambda that configure custom effect
     */
    fun customEffects(build: PotionEffectBuilder.() -> Unit) {
        val builder = PotionEffectBuilder()
        builder.build()
        itemMeta.addCustomEffect(builder.toPotionEffect(), true)
    }
}

/**
 * Configure potion data
 */
@ItemDslMarker
@Suppress("PropertyName")
class PotionDataBuilder {
    /**
     * Type of potion
     */
    var type: PotionType? = null

    private var isExtended: Boolean = false
    private var isUpgraded: Boolean = false

    /**
     * Extend potion effect
     */
    val Extended: Unit
        get() {
            isExtended = true
        }

    /**
     * Upgrade potion effect
     */
    val Upgraded: Unit
        get() {
            isUpgraded = true
        }

    /**
     * Create potionData from this builder
     * @return PotionData configured by this builder
     */
    fun toPotionData(): PotionData = PotionData(type, isExtended, isUpgraded)
}

/**
 * Configure potion effect
 */
@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
@ItemDslMarker
class PotionEffectBuilder {
    /**
     * Type of potion effect
     */
    var type: PotionEffectType? = null

    /**
     * Duration of potion effect
     */
    var duration: Int = 0

    /**
     * Amplifier of potion effect
     */
    var amplifier: Int = 1

    /**
     * Color of potion effect
     */
    var color: Color? = null

    private var isAmbient: Boolean = false
    private var isParticles: Boolean = false

    /**
     * Set this potion ambient
     */
    val Ambient: Unit
        get() {
            isAmbient = true
        }

    /**
     * Set this potion has particle
     */
    val Particles: Unit
        get() {
            isParticles = true
        }

    /**
     * Create PotionEffect from this builder
     * @return PotionEffect configured by this builder
     */
    fun toPotionEffect(): PotionEffect = if (color == null) PotionEffect(type, duration, amplifier, isAmbient, isParticles)
    else PotionEffect(type, duration, amplifier, isAmbient, isParticles, color)
}