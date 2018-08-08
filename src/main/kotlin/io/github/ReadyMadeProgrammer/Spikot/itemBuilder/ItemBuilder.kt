package io.github.ReadyMadeProgrammer.Spikot.itemBuilder

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.material.MaterialData

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class ItemDslMarker

fun item(material: Material, build: ItemBuilder.() -> Unit = {}): ItemStack {
    val builder = ItemBuilder(material)
    builder.build()
    return builder.toItemStack()
}

fun banner(build: BannerItemBuilder.() -> Unit): ItemStack {
    val builder = BannerItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun book(build: BookItemBuilder.() -> Unit): ItemStack {
    val builder = BookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun firework(build: FireworkItemBuilder.() -> Unit): ItemStack {
    val builder = FireworkItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun enchantBook(build: EnchantBookItemBuilder.() -> Unit): ItemStack {
    val builder = EnchantBookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun leatherHelment(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_HELMET)
    builder.build()
    return builder.toItemStack()
}

fun leatherChestPlate(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_CHESTPLATE)
    builder.build()
    return builder.toItemStack()
}

fun leatherLeggings(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_LEGGINGS)
    builder.build()
    return builder.toItemStack()
}

fun leatherBoots(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_BOOTS)
    builder.build()
    return builder.toItemStack()
}

fun potion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.POTION)
    builder.build()
    return builder.toItemStack()
}

fun splashPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.SPLASH_POTION)
    builder.build()
    return builder.toItemStack()
}

fun lingeringPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.LINGERING_POTION)
    builder.build()
    return builder.toItemStack()
}

fun skull(build: SkullItemBuilder.() -> Unit): ItemStack {
    val builder = SkullItemBuilder()
    builder.build()
    return builder.toItemStack()
}

fun spawnEgg(build: SpawnEggItemBuilder.() -> Unit): ItemStack {
    val builder = SpawnEggItemBuilder()
    builder.build()
    return builder.toItemStack()
}

@ItemDslMarker
open class ItemBuilder(material: Material) {
    protected val item = ItemStack(material)
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
    var meta: ItemMeta
        get() = item.itemMeta
        set(value) {
            item.itemMeta = value
        }
    var data: MaterialData
        get() = item.data
        set(value) {
            item.data = value
        }
    var displayName: String
        get() = item.itemMeta.displayName
        set(value) {
            item.itemMeta.displayName = value
        }
    var localizedName: String
        get() = item.itemMeta.localizedName
        set(value) {
            item.itemMeta.localizedName = value
        }

    val UnBreakable: Unit
        get() {
            item.itemMeta.isUnbreakable = true
        }

    fun flags(build: ItemFlagBuilder.() -> Unit) {
        val builder = ItemFlagBuilder(item)
        builder.build()
    }

    fun enchantments(build: EnchantmentBuilder.() -> Unit) {
        val builder = EnchantmentBuilder(item)
        builder.build()
    }

    fun toItemStack() = item
}

@ItemDslMarker
class ItemFlagBuilder(itemStack: ItemStack) {
    private val itemFlag = itemStack.itemMeta.itemFlags
    val HIDE_ENCHANT: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_ENCHANTS)
        }
    val HIDE_ATTRIBUTES: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_ATTRIBUTES)
        }
    val HIDE_UNBREAKABLE: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_UNBREAKABLE)
        }
    val HIDE_DESTROYS: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_DESTROYS)
        }
    val HIDE_PLACED_ON: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_PLACED_ON)
        }
    val HIDE_POTION_EFFECTS: Unit
        get() {
            itemFlag.add(ItemFlag.HIDE_POTION_EFFECTS)
        }
}

@ItemDslMarker
class EnchantmentBuilder(private val itemStack: ItemStack) {
    operator fun Enchantment.invoke(level: Int) {
        itemStack.addEnchantment(this, level)
    }
}