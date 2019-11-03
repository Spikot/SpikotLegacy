@file:Suppress("unused", "PropertyName")

package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class ItemDslMarker

open class DefaultItemBuilder(item: ItemStack) : ItemBuilder<ItemMetaBuilder<ItemMeta>>(item) {
    constructor(material: Material) : this(ItemStack(material))

    override fun meta(build: ItemMetaBuilder<ItemMeta>.() -> Unit) {
        val builder = ItemMetaBuilder<ItemMeta>(item.itemMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

@ItemDslMarker
abstract class ItemBuilder<T : ItemMetaBuilder<*>>(protected val item: ItemStack) {
    constructor(material: Material) : this(ItemStack(material))

    val material: Material
        get() = item.type

    var amount: Int
        get() = item.amount
        set(value) {
            item.amount = value
        }

    var durability: Short
        get() = item.durability
        set(value) {
            item.durability = value
        }

    fun enchantments(build: EnchantmentBuilder.() -> Unit) {
        val builder = EnchantmentBuilder(item)
        builder.build()
    }

    abstract fun meta(build: T.() -> Unit)

    fun toItemStack() = item
}
