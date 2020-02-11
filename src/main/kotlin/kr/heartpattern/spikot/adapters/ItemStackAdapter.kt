/*
 * Copyright 2020 Spikot project authors
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

package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.nbt.WrapperNBTCompound
import org.bukkit.inventory.ItemStack

inline class ItemStack(val value: ItemStack)

/**
 * Adapter to handle nms ItemStack
 */
interface ItemStackAdapter : IAdapter {
    /**
     * Check if ItemStack is CraftItemStack
     * @param itemStack ItemStack to be checked
     * @return Whether given ItemStack is CraftItemStack
     */
    fun isCraftItemStack(itemStack: ItemStack): Boolean

    /**
     * Convert ItemStack to CraftItemStack.
     * If ItemStack is not ItemStack, new CraftItemStack will return.
     * If ItemStack is already CraftItemStack, given ItemStack will return.
     * @param itemStack ItemStack to be converted
     * @return Converted ItemStack
     */
    fun toCraftItemStack(itemStack: ItemStack): ItemStack

    /**
     * Check ItemStack has tag
     * @param itemStack ItemStack to be checked
     * @return Whether given ItemStack has tag
     */
    fun hasTag(itemStack: ItemStack): Boolean

    /**
     * Get wrapped nbt compound for given ItemStack
     * @param itemStack ItemStack to extract nbt tag compound
     * @return Wrapped nbt compound from given ItemStack. If ItemStack does not have tag then return null.
     */
    fun getWrappedTag(itemStack: ItemStack): WrapperNBTCompound?

    /**
     * Dump ItemStack to NBTCompound.
     * @param itemStack ItemStack to dump
     * @return Dumped WrappedNBTCompound
     */
    fun toNBTCompound(itemStack: ItemStack): WrapperNBTCompound

    /**
     * Restore ItemStack from NBTCompound.
     * @param nbt WrapperNBTCompound which contains ItemStack information
     * @return ItemStack created by given tag
     */
    fun fromNBTCompound(nbt: WrapperNBTCompound): ItemStack

    @Module
    object Resolver : VersionAdapterResolver<ItemStackAdapter>(ItemStackAdapter::class, VersionType.BUKKIT)

    companion object : ItemStackAdapter by Resolver.default
}