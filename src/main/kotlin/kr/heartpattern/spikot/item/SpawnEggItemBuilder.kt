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

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SpawnEggMeta

/**
 * Configure spawn egg item
 */
class SpawnEggItemBuilder(itemStack: ItemStack) : ItemBuilder<SpawnEggItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.MONSTER_EGG))

    override fun meta(build: SpawnEggItemMetaBuilder.() -> Unit) {
        val builder = SpawnEggItemMetaBuilder(item.itemMeta as SpawnEggMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }

}

/**
 * Configure spawn egg item meta
 */
class SpawnEggItemMetaBuilder(itemMeta: SpawnEggMeta) : ItemMetaBuilder<SpawnEggMeta>(itemMeta) {
    /**
     * EntityType this spawn egg spawn
     */
    var spawnedType: EntityType
        get() = itemMeta.spawnedType
        set(value) {
            itemMeta.spawnedType = value
        }
}