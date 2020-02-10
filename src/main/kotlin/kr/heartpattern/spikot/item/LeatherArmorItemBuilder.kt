/*
 * Copyright 2020 HeartPattern
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