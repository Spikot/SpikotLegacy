package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.nbt.WrapperNBTCompound
import org.bukkit.inventory.ItemStack

interface ItemStackAdapter : IAdapter {
    fun isCraftItemStack(itemStack: ItemStack): Boolean
    fun toCraftItemStack(itemStack: ItemStack): ItemStack
    fun hasTag(itemStack: ItemStack): Boolean
    fun getWrappedTag(itemStack: ItemStack): WrapperNBTCompound?
    fun toNBTCompound(itemStack: ItemStack): WrapperNBTCompound
    fun fromNBTCompound(nbt: WrapperNBTCompound): ItemStack

    object Resolver : VersionAdapterResolver<ItemStackAdapter>(ItemStackAdapter::class, VersionType.BUKKIT)
    companion object : ItemStackAdapter by Resolver.default
}