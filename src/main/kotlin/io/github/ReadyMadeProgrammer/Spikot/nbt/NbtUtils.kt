package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.reflections.ReflectionUtils
import org.bukkit.inventory.ItemStack

val nbtTagCompoundClass = ReflectionUtils.getNmsClass("NBTTagCompound")
val nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor()
val craftItemStackClass = ReflectionUtils.getCraftClass("inventory.CraftItemStack")
val craftItemStackNmsCopyMethod = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack::class.java)
val nmsItemStackClass = ReflectionUtils.getNmsClass("ItemStack")
val nmsItemStackSaveMethod = nmsItemStackClass.getDeclaredMethod("save", nbtTagCompoundClass)

fun ItemStack.getJson(): String {
    val nbtTagCompound = nbtTagCompoundConstructor.newInstance()
    val nmsItemStack = craftItemStackNmsCopyMethod.invoke(null, this)
    val data = nmsItemStackSaveMethod.invoke(nmsItemStack, nbtTagCompound)
    return data.toString()
}