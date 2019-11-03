package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.ItemStackAdapter
import org.bukkit.inventory.ItemStack

val ItemStack.isCraftItemStack: Boolean
    get() = ItemStackAdapter.isCraftItemStack(this)

fun ItemStack.toCraftItemStack(): ItemStack {
    return ItemStackAdapter.toCraftItemStack(this)
}

val ItemStack.hasTag: Boolean
    get() {
        return ItemStackAdapter.hasTag(this)
    }

fun ItemStack.getWrappedTag(): WrapperNBTCompound {
    return ItemStackAdapter.getWrappedTag(this)
}