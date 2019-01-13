package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class BookItemBuilder(itemStack: ItemStack) : ItemBuilder<BookItemMetaBuilder>(itemStack) {
    constructor() : this(ItemStack(Material.BOOK))

    override fun meta(build: BookItemMetaBuilder.() -> Unit) {
        val builder = BookItemMetaBuilder(item.itemMeta as BookMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }

}

class BookItemMetaBuilder(itemMeta: BookMeta) : ItemMetaBuilder<BookMeta>(itemMeta) {
    var author: String
        get() = itemMeta.author
        set(value) {
            itemMeta.author = value
        }
    var title: String
        get() = itemMeta.title
        set(value) {
            itemMeta.title = value
        }

    fun pages(build: PageBuilder.() -> Unit) {
        val builder = PageBuilder(itemMeta)
        builder.build()
    }
}

@ItemDslMarker
class PageBuilder(private val itemMeta: BookMeta) {
    operator fun String.unaryPlus() {
        itemMeta.addPage(this)
    }
}