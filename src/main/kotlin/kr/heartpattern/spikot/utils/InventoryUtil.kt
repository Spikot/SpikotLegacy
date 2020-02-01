@file:Suppress("unused")

package kr.heartpattern.spikot.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Check all of given [items] can be added into inventory
 * @receiver Inventory to check
 * @param items Collection of items to check
 * @return true if all of item can be added, false if any of item cannot be added.
 */
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

/**
 * Give all of item or none of item
 * @receiver Inventory to add item
 * @param item Collection of item to give
 * @return Whether add is succeed
 */
fun Inventory.giveAll(item: Collection<ItemStack>): Boolean {
    return if (canGive(item)) {
        addItem(*item.toTypedArray())
        true
    } else {
        false
    }
}

/**
 * Count number of item in inventory.
 * @receiver Inventory to check
 * @param item ItemStack to check. Amount is ignored.
 * @return Number of [item] in inventory
 */
fun Inventory.count(item: ItemStack): Int {
    return contents.sumBy {
        if (it.isSimilar(item))
            it.amount
        else
            0
    }
}