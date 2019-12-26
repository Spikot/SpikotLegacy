@file:Suppress("unused", "PropertyName")

package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * DSL Marker for ItemBuilder
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class ItemDslMarker

/**
 * Configure default item
 */
open class DefaultItemBuilder(item: ItemStack) : ItemBuilder<ItemMetaBuilder<ItemMeta>>(item) {
    constructor(material: Material) : this(ItemStack(material))

    override fun meta(build: ItemMetaBuilder<ItemMeta>.() -> Unit) {
        val builder = ItemMetaBuilder<ItemMeta>(item.itemMeta)
        builder.build()
        item.itemMeta = builder.itemMeta
    }
}

/**
 * Superclass of all item builder
 * @param item Item to configure
 */
@ItemDslMarker
abstract class ItemBuilder<T : ItemMetaBuilder<*>>(protected val item: ItemStack) {
    /**
     * Create new ItemStack with given material
     * @param material Material type of item
     */
    constructor(material: Material) : this(ItemStack(material))

    /**
     * Material of item
     */
    val material: Material
        get() = item.type

    /**
     * Amount of item
     */
    var amount: Int
        get() = item.amount
        set(value) {
            item.amount = value
        }

    /**
     * Durability of item
     */
    var durability: Short
        get() = item.durability
        set(value) {
            item.durability = value
        }

    /**
     * Configure enchantments
     * @param build lambda that configure enchantment
     */
    fun enchantments(build: EnchantmentBuilder.() -> Unit) {
        val builder = EnchantmentBuilder(item)
        builder.build()
    }

    /**
     * Configure meta
     * @param build lambda that configure meta
     */
    abstract fun meta(build: T.() -> Unit)

    /**
     * Get ItemStack from builder
     * @return ItemStack configured by this builder
     */
    fun toItemStack() = item
}
