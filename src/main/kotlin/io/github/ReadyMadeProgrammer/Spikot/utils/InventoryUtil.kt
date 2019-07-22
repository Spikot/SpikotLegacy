@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.canGive(items: Collection<ItemStack>): Boolean {
    class ItemPair(val item: ItemStack, var count: Int)

    var emptySlot = 0
    val storageItem = HashSet<ItemPair>()
    for (item in storageContents) {
        if (item == null || item.type == Material.AIR || item.amount <= 0) {
            emptySlot++
            continue
        }
        if (item.maxStackSize <= item.amount)
            continue
        storageItem.add(ItemPair(item, item.maxStackSize - item.amount))
    }

    val remain = HashSet<ItemPair>()
    outer@ for (item in items) {
        if (item.type == Material.AIR || item.amount <= 0)
            continue
        var remainAmount = item.amount
        val iter = storageItem.iterator()
        while (iter.hasNext()) {
            val curr = iter.next()
            if (curr.item.isSimilar(item)) {
                if (curr.count > remainAmount) {
                    curr.count -= remainAmount
                    continue@outer
                } else if (curr.count == remainAmount) {
                    iter.remove()
                    continue@outer
                } else {
                    remainAmount -= curr.count
                    iter.remove()
                }
            }
        }
        if (remainAmount > 0) {
            for (remained in remain) {
                if (remained.item.isSimilar(item)) {
                    remained.count += remainAmount
                    continue@outer
                }
            }
            remain.add(ItemPair(item, remainAmount))
        }
    }

    var requiredSlot = 0
    for (item in remain) {
        val maxStack = item.item.maxStackSize
        requiredSlot += if (item.count % maxStack == 0) {
            item.count / maxStack
        } else {
            item.count / maxStack + 1
        }
    }

    return emptySlot >= requiredSlot
}

fun Inventory.giveAll(item: Collection<ItemStack>): Boolean {
    return if (canGive(item)) {
        addItem(*item.toTypedArray())
        true
    } else {
        false
    }
}