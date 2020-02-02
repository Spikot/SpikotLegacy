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

package kr.heartpattern.spikot.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack


/**
 * Create new item
 * @param material Material type of this item
 * @param build lambda that configure ItemStack
 * @return New item stack
 */
fun item(material: Material, build: DefaultItemBuilder.() -> Unit = {}): ItemStack {
    val builder = DefaultItemBuilder(material)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given item
 * @param item ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified item stack
 */
fun item(item: ItemStack, build: DefaultItemBuilder.() -> Unit = {}): ItemStack {
    val builder = DefaultItemBuilder(item)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new banner
 * @param build lambda that configure ItemStack
 * @return New banner
 */
fun banner(build: BannerItemBuilder.() -> Unit): ItemStack {
    val builder = BannerItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given banner
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified banner
 */
fun banner(itemStack: ItemStack, build: BannerItemBuilder.() -> Unit): ItemStack {
    val builder = BannerItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new book
 * @param build lambda that configure ItemStack
 * @return New book
 */
fun book(build: BookItemBuilder.() -> Unit): ItemStack {
    val builder = BookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given book
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified book
 */
fun book(itemStack: ItemStack, build: BookItemBuilder.() -> Unit): ItemStack {
    val builder = BookItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new firework
 * @param build lambda that configure ItemStack
 * @return New firework
 */
fun firework(build: FireworkItemBuilder.() -> Unit): ItemStack {
    val builder = FireworkItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given firework
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified firework
 */
fun firework(itemStack: ItemStack, build: FireworkItemBuilder.() -> Unit): ItemStack {
    val builder = FireworkItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new enchant book
 * @param build lambda that configure ItemStack
 * @return New enchant book
 */
fun enchantBook(build: EnchantBookItemBuilder.() -> Unit): ItemStack {
    val builder = EnchantBookItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given enchant book
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified enchant book
 */
fun enchantBook(itemStack: ItemStack, build: EnchantBookItemBuilder.() -> Unit): ItemStack {
    val builder = EnchantBookItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new leather helmet
 * @param build lambda that configure ItemStack
 * @return New leather helmet
 */
fun leatherHelmet(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_HELMET)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given leather helmet
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified leather helmet
 */
fun leatherHelmet(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new leather chest plate
 * @param build lambda that configure ItemStack
 * @return New leather chest plate
 */
fun leatherChestPlate(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_CHESTPLATE)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given leather chest plate
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified leather chest plate
 */
fun leatherChestPlate(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new leather leggings
 * @param build lambda that configure ItemStack
 * @return New leather leggings
 */
fun leatherLeggings(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_LEGGINGS)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given leather leggings
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified leather leggings
 */
fun leatherLeggings(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new leather boots
 * @param build lambda that configure ItemStack
 * @return New leather boots
 */
fun leatherBoots(build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(Material.LEATHER_BOOTS)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given leather boots
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified leather boots
 */
fun leatherBoots(itemStack: ItemStack, build: LeatherArmorItemBuilder.() -> Unit): ItemStack {
    val builder = LeatherArmorItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new potion
 * @param build lambda that configure ItemStack
 * @return New potion
 */
fun potion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.POTION)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given potion
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified potion
 */
fun potion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new splash potion
 * @param build lambda that configure ItemStack
 * @return New splash potion
 */
fun splashPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.SPLASH_POTION)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given splash potion
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified splash potion
 */
fun splashPotion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new lingering potion
 * @param build lambda that configure ItemStack
 * @return New lingering potion
 */
fun lingeringPotion(build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(Material.LINGERING_POTION)
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given lingering potion
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified lingering potion
 */
fun lingeringPotion(itemStack: ItemStack, build: PotionItemBuilder.() -> Unit): ItemStack {
    val builder = PotionItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new skull
 * @param build lambda that configure ItemStack
 * @return New skull
 */
fun skull(build: SkullItemBuilder.() -> Unit): ItemStack {
    val builder = SkullItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given skull
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified skull
 */
fun skull(itemStack: ItemStack, build: SkullItemBuilder.() -> Unit): ItemStack {
    val builder = SkullItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}

/**
 * Create new spawn egg
 * @param build lambda that configure ItemStack
 * @return New spawn egg
 */
fun spawnEgg(build: SpawnEggItemBuilder.() -> Unit): ItemStack {
    val builder = SpawnEggItemBuilder()
    builder.build()
    return builder.toItemStack()
}

/**
 * Modify given spawn egg
 * @param itemStack ItemStack to modify
 * @param build lambda that modify ItemStack
 * @return Modified spawn egg
 */
fun spawnEgg(itemStack: ItemStack, build: SpawnEggItemBuilder.() -> Unit): ItemStack {
    val builder = SpawnEggItemBuilder(itemStack)
    builder.build()
    return builder.toItemStack()
}