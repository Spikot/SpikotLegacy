package kr.heartpattern.spikot.nbt

import kr.heartpattern.spikot.adapters.ItemStackAdapter
import org.bukkit.inventory.ItemStack

/**
 * Represent CraftItemStack
 */
inline class WrapperCraftItemStack(val itemStack: ItemStack)

/**
 * Check if ItemStack is CraftItemStack
 */
val ItemStack.isCraftItemStack: Boolean
    get() = ItemStackAdapter.isCraftItemStack(this)

/**
 *  Convert to CraftItemStack. Return new CraftItemStack if this is not CraftItemStack, or else return this.
 *  @receiver ItemStack to convert.
 *  @receiver Converted ItemStack
 */
fun ItemStack.toCraftItemStack(): WrapperCraftItemStack {
    return WrapperCraftItemStack(ItemStackAdapter.toCraftItemStack(this))
}

/**
 * Check if CraftItemStack has tag
 */
val WrapperCraftItemStack.hasTag: Boolean
    get() {
        return ItemStackAdapter.hasTag(this.itemStack)
    }

/**
 * Get wrapped nbt tag of item
 * @receiver CraftItemStack to extract
 * @return Wrapped nbt compound if tag exists, otherwise null.
 */
fun WrapperCraftItemStack.getWrappedTag(): WrapperNBTCompound? {
    return ItemStackAdapter.getWrappedTag(this.itemStack)
}