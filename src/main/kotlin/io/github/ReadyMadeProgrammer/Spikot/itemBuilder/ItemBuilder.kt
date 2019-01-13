@file:Suppress("unused", "PropertyName")

package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import io.github.ReadyMadeProgrammer.Spikot.utils.InputBuilder
import io.github.ReadyMadeProgrammer.Spikot.utils.plus
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class ItemDslMarker

fun item(material: Material, build: DefaultItemBuilder.() -> Unit = {}): ItemStack {
    val builder = DefaultItemBuilder(material)
    builder.build()
    return builder.toItemStack()
}

fun item(item: ItemStack, build: DefaultItemBuilder.() -> Unit = {}): ItemStack {
    val builder = DefaultItemBuilder(item)
    builder.build()
    return builder.toItemStack()
}

fun banner(build: BannerItemBuilder.() -> Unit): ItemStack {
    val builder = BannerItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun banner(itemStack: ItemStack, build: BannerItemBuilder.() -> Unit): ItemStack {
    val builder = BannerItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun book(build: BookItemBuilder.() -> Unit): ItemStack {
    val builder = BookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun book(itemStack: ItemStack, build: BookItemBuilder.() -> Unit): ItemStack {
    val builder = BookItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun firework(build: FireworkItemBuilder.() -> Unit): ItemStack {
    val builder = FireworkItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun firework(itemStack: ItemStack, build: FireworkItemBuilder.() -> Unit): ItemStack {
    val builder = FireworkItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun enchantBook(build: EnchantBookItemBuilder.() -> Unit): ItemStack {
    val builder = EnchantBookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun enchantBook(itemStack: ItemStack, build: EnchantBookItemBuilder.() -> Unit): ItemStack {
    val builder = EnchantBookItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun leatherHelmet(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_HELMET)
    builder.build()
    return builder.toItemStack()
}

fun leatherHelmet(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun leatherChestPlate(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_CHESTPLATE)
    builder.build()
    return builder.toItemStack()
}

fun leatherChestPlate(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun leatherLeggings(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_LEGGINGS)
    builder.build()
    return builder.toItemStack()
}

fun leatherLeggings(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun leatherBoots(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_BOOTS)
    builder.build()
    return builder.toItemStack()
}

fun leatherBoots(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun potion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.POTION)
    builder.build()
    return builder.toItemStack()
}

fun potion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun splashPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.SPLASH_POTION)
    builder.build()
    return builder.toItemStack()
}

fun splashPotion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun lingeringPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.LINGERING_POTION)
    builder.build()
    return builder.toItemStack()
}

fun lingeringPotion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun skull(build: SkullItemBuilder.() -> Unit): ItemStack {
    val builder = SkullItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun skull(itemStack: ItemStack, build: SkullItemBuilder.() -> Unit): ItemStack {
    val builder = SkullItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

fun spawnEgg(build: SpawnEggItemBuilder.() -> Unit): ItemStack {
    val builder = SpawnEggItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun spawnEgg(itemStack: ItemStack, build: SpawnEggItemBuilder.() -> Unit): ItemStack {
    val builder = SpawnEggItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

class DefaultItemBuilder(item: ItemStack) : ItemBuilder<ItemMetaBuilder<ItemMeta>>(item) {
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

@ItemDslMarker
class EnchantmentBuilder(private val itemStack: ItemStack) {
    operator fun Enchantment.invoke(level: Int) {
        itemStack.addUnsafeEnchantment(this, level)
    }
}

@ItemDslMarker
open class ItemMetaBuilder<T : ItemMeta>(internal val itemMeta: T) {
    val UnBreakable: Unit
        get() {
            itemMeta.isUnbreakable = true
        }

    var displayName: String
        get() = itemMeta.displayName
        set(value) {
            itemMeta.displayName = ChatColor.RESET + value
        }
    var localizedName: String
        get() = itemMeta.localizedName
        set(value) {
            itemMeta.displayName = value
        }

    fun lore(build: InputBuilder<String>.() -> Unit) {
        val builder = InputBuilder<String>()
        builder.build()
        itemMeta.lore = builder.data.map { ChatColor.RESET + it }
    }

    fun itemFlag(build: ItemFlagBuilder.() -> Unit) {
        val builder = ItemFlagBuilder(itemMeta)
        builder.build()
    }
}
