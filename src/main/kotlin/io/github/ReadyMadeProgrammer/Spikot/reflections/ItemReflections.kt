package io.github.ReadyMadeProgrammer.Spikot.reflections

import org.bukkit.inventory.ItemStack

internal val classCraftItemStack = ReflectionUtils.getCraftClass("inventory.CraftItemStack")
internal val classNmsItemStack = ReflectionUtils.getNmsClass("ItemStack")
internal val classNbtTagCompound = ReflectionUtils.getNmsClass("NBTTagCompound")
internal val methodAsNMSCopy = classCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack::class.java)
internal val methodAsBukkitCopy = classCraftItemStack.getDeclaredMethod("asBukkitCopy", classNmsItemStack)
internal val methodGetTag = classNmsItemStack.getDeclaredMethod("getTag")
internal val methodSetTag = classNmsItemStack.getDeclaredMethod("setTag", classNbtTagCompound)

fun ItemStack.toNmsCopy(): NmsItemStack {
    return NmsItemStack(methodAsNMSCopy.invoke(null, this))
}

fun NmsItemStack.toBukkitCopy(): ItemStack {
    return methodAsBukkitCopy.invoke(null, this.handle) as ItemStack
}

var NmsItemStack.tag: NmsNbtTagCompound
    get() = NmsNbtTagCompound(methodGetTag.invoke(this.handle) ?: constructorNmsNbtTagCompound.newInstance())
    set(value) {
        methodSetTag.invoke(this, value.handle)
    }