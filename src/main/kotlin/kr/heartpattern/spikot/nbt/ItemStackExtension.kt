package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.ItemStackAdapter
import org.bukkit.inventory.ItemStack

inline class WrapperCraftItemStack(val itemStack: ItemStack)

val ItemStack.isCraftItemStack: Boolean
    get() = ItemStackAdapter.isCraftItemStack(this)

fun ItemStack.toCraftItemStack(): WrapperCraftItemStack {
    return WrapperCraftItemStack(ItemStackAdapter.toCraftItemStack(this))
}

val WrapperCraftItemStack.hasTag: Boolean
    get() {
        return ItemStackAdapter.hasTag(this.itemStack)
    }

fun WrapperCraftItemStack.getWrappedTag(): WrapperNBTCompound? {
    return ItemStackAdapter.getWrappedTag(this.itemStack)
}