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

import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

/**
 * Configure flag of ItemStack
 */
@ItemDslMarker
class ItemFlagBuilder(private val itemMeta: ItemMeta) {
    val HIDE_ENCHANT: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    val HIDE_ATTRIBUTES: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    val HIDE_UNBREAKABLE: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
    val HIDE_DESTROYS: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS)
        }
    val HIDE_PLACED_ON: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON)
        }
    val HIDE_POTION_EFFECTS: Unit
        get() {
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        }
}