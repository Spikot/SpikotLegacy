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

package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

/**
 * Configure enchant book item
 */
class EnchantBookItemBuilder(itemStack: ItemStack) : ItemBuilder<EnchantBookItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.ENCHANTED_BOOK))

    override fun meta(build: EnchantBookItemMetaBuilder.() -> Unit) {
        val builder = EnchantBookItemMetaBuilder(item.itemMeta as EnchantmentStorageMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Configure enchant book item meta
 */
@ItemDslMarker
class EnchantBookItemMetaBuilder(itemMeta: EnchantmentStorageMeta) : ItemMetaBuilder<EnchantmentStorageMeta>(itemMeta) {
    /**
     * Add new enchantment
     * @param level Enchantment level
     */
    operator fun Enchantment.invoke(level: Int) {
        itemMeta.addEnchant(this, level, true)
    }
}