package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack


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