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

import kr.heartpattern.spikot.utils.ListBuilder
import kr.heartpattern.spikot.utils.plus
import org.bukkit.ChatColor
import org.bukkit.inventory.meta.ItemMeta

/**
 * Configure ItemMeta
 */
@ItemDslMarker
open class ItemMetaBuilder<T : ItemMeta>(internal val itemMeta: T) {

    /**
     * Add unbreakable attribute to item
     */
    @Suppress("PropertyName")
    val UnBreakable: Unit
        get() {
            itemMeta.isUnbreakable = true
        }

    /**
     * Display name of item
     */
    var displayName: String
        get() = itemMeta.displayName
        set(value) {
            itemMeta.displayName = ChatColor.RESET + value
        }

    /**
     * Localized name of item
     */
    var localizedName: String
        get() = itemMeta.localizedName
        set(value) {
            itemMeta.displayName = value
        }

    /**
     * Configure lore of item
     * @param build lambda that configure lore
     */
    fun lore(build: ListBuilder<String>.() -> Unit) {
        val builder = ListBuilder<String>()
        builder.build()
        itemMeta.lore = builder.data.map { ChatColor.RESET + it }
    }

    /**
     * Configure flag of item,
     * @param build lambda that configure lore
     */
    fun itemFlag(build: ItemFlagBuilder.() -> Unit) {
        val builder = ItemFlagBuilder(itemMeta)
        builder.build()
    }
}