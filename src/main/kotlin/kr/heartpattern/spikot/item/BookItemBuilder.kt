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
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

/**
 * Configure book item
 */
class BookItemBuilder(itemStack: ItemStack) : ItemBuilder<BookItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.BOOK))

    override fun meta(build: BookItemMetaBuilder.() -> Unit) {
        val builder = BookItemMetaBuilder(item.itemMeta as BookMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }

}

/**
 * Configure book item meta
 */
class BookItemMetaBuilder(itemMeta: BookMeta) : ItemMetaBuilder<BookMeta>(itemMeta) {
    /**
     * Author of book
     */
    var author: String
        get() = itemMeta.author
        set(value) {
            itemMeta.author = value
        }

    /**
     * Title of book
     */
    var title: String
        get() = itemMeta.title
        set(value) {
            itemMeta.title = value
        }

    /**
     * Configure pages of book
     */
    fun pages(build: PageBuilder.() -> Unit) {
        val builder = PageBuilder(itemMeta)
        builder.build()
    }
}

/**
 * Configure pages of book
 */
@ItemDslMarker
class PageBuilder(private val itemMeta: BookMeta) {
    /**
     * Add new page
     */
    operator fun String.unaryPlus() {
        itemMeta.addPage(this)
    }
}