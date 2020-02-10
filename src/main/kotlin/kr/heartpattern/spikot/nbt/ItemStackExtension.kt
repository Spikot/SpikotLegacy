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