@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.canGive(items: Collection<ItemStack>): Boolean {
    class ItemPair(val item: ItemStack, var count: Int)

    val everyItem = HashSet<ItemPair>(size + items.size)
    outer@ for (item in items) {
        if (item.type == Material.AIR || item.amount <= 0)
            continue@outer
        for (already in everyItem) {
            if (already.item.isSimilar(item)) {
                already.count += item.amount
                continue@outer
            }
        }
        everyItem.add(ItemPair(item, item.amount))
    }

    var overSized = 0
    outer@ for (item in storageContents) {
        if (item == null || item.type == Material.AIR || item.amount <= 0)
            continue@outer
        if (item.amount > item.maxStackSize) {
            overSized++
            continue@outer
        }
        for (already in everyItem) {
            if (already.item.isSimilar(item)) {
                already.count += item.amount
                continue@outer
            }
        }
        everyItem.add(ItemPair(item, item.amount))
    }

    var occupedStack = 0
    for (item in everyItem) {
        occupedStack += if (item.count % item.item.maxStackSize == 0) item.count / item.item.maxStackSize
        else item.count / item.item.maxStackSize + 1
    }

    return overSized + occupedStack <= size
}

fun Inventory.giveAll(item: Collection<ItemStack>): Boolean {
    return if (canGive(item)) {
        addItem(*item.toTypedArray())
        true
    } else {
        false
    }
}