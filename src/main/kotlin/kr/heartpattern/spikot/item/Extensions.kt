@file:UseExperimental(ExperimentalContracts::class)

package kr.heartpattern.spikot.item

import kr.heartpattern.spikot.adapters.ItemStackAdapter
import kr.heartpattern.spikot.adapters.NBTAdapter
import kr.heartpattern.spikot.nbt.toCraftItemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun ItemStack?.isEmpty(): Boolean {
    contract { returns(false) implies (this@isEmpty != null) }
    return this == null || type == Material.AIR || amount == 0
}

fun ItemStack?.isNotEmpty(): Boolean {
    contract { returns(true) implies (this@isNotEmpty != null) }
    return !isEmpty()
}

fun ItemStack.encode(): ByteArray {
    return NBTAdapter.compressNBT(ItemStackAdapter.toNBTCompound(toCraftItemStack().itemStack))
}

fun decodeItemStack(bytes: ByteArray): ItemStack {
    return ItemStackAdapter.fromNBTCompound(NBTAdapter.decompressNBT(bytes))
}