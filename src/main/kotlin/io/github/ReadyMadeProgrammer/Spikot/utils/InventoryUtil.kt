@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.max

fun Inventory.canGive(rawItem: Set<ItemStack>): Boolean {
    val items = HashMap<ItemStack, Int>()
    rawItem.forEach { item ->
        val cloned = item.clone()
        items[cloned] = item.amount + if (items.containsKey(cloned)) items[cloned]!! else 0
    }
    var empty = 0
    storageContents.forEach { item ->
        if (item == null || item.type == Material.AIR) {
            empty++
        } else {
            val cloned = item.clone()
            cloned.amount = 1
            if (items.contains(cloned)) {
                items[cloned] = max(0, items[cloned]!! - max(item.type.maxStackSize - item.amount, 0))
            }
        }
    }
    return items.asSequence().filter { it.value != 0 }.map { (it.value + 1) / it.key.maxStackSize }.sum() <= empty
}

fun Inventory.giveAll(item: Set<ItemStack>): Boolean {
    return if (canGive(item)) {
        addItem(*item.toTypedArray())
        true
    } else {
        false
    }
}