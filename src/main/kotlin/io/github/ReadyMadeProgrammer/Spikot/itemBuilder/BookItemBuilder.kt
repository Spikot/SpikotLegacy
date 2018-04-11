package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta


class BookItemBuilder: ItemBuilder(Material.BOOK){
    val bookMeta = item.itemMeta as BookMeta
    var author: String
        get()=bookMeta.author
        set(value){
            bookMeta.author = value
        }
    var title: String
        get()=bookMeta.title
        set(value){
            bookMeta.title = value
        }
    fun pages(build: PageBuilder.()->Unit){
        val builder = PageBuilder(item)
        builder.build()
    }
}

@ItemDslMarker
class PageBuilder(book: ItemStack){
    private val meta = book.itemMeta as BookMeta
    operator fun String.unaryPlus(){
        meta.addPage(this)
    }
}