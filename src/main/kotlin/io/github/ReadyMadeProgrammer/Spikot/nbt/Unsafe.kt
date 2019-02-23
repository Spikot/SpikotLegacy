package io.github.ReadyMadeProgrammer.Spikot.nbt

import org.bukkit.inventory.ItemStack

fun ItemStack.toCraftItemStack(): CraftItemStack {
    return if (this is CraftItemStack) {
        this
    } else {
        CraftItemStack.asCraftCopy(this)
    }
}

fun CraftItemStack.hasTag(key: String): Boolean {
    return this.tag.hasKeyOfType(key, TagType.TAG.id)
}

inline fun <reified T : NbtAccessor> CraftItemStack.getTag(key: String): T {
    val constructor =
            T::class.constructors.find { it.parameters.size == 1 && it.parameters[0].type == NBTTagCompound::class }
                    ?: throw NoSuchMethodException("Cannot find constructor with NBTTagCompound parameter")
    val value = this.tag.getCompound(key)
    if (!this.tag.hasKeyOfType(key, TagType.TAG.id)) {
        this.tag.setCompound(key, value)
    }
    return constructor.call(value)
}