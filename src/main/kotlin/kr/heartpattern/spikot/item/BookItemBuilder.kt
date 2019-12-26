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