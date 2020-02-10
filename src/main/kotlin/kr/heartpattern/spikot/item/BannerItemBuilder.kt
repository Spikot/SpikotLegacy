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

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

/**
 * Configure banner item meta
 */
class BannerItemMetaBuilder(itemMeta: BannerMeta) : ItemMetaBuilder<BannerMeta>(itemMeta) {
    /**
     * Add new pattern to this banner
     */
    operator fun PatternType.invoke(color: DyeColor) {
        itemMeta.addPattern(Pattern(color, this))
    }
}

/**
 * Configure banner item
 */
class BannerItemBuilder(item: ItemStack) : ItemBuilder<BannerItemMetaBuilder>(item) {
    constructor() : this(ItemStack(Material.BANNER))

    override fun meta(build: BannerItemMetaBuilder.() -> Unit) {
        val builder = BannerItemMetaBuilder(item.itemMeta as BannerMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}