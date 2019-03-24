package io.github.ReadyMadeProgrammer.Spikot.nbt

import io.github.ReadyMadeProgrammer.Spikot.CraftItemStack
import io.github.ReadyMadeProgrammer.Spikot.NBTTagCompound
import io.github.ReadyMadeProgrammer.Spikot.misc.Value
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

private val constructors = HashMap<KClass<*>, Value<KFunction<*>?>>()

@PublishedApi
internal fun <T : Any> queryConstructor(type: KClass<T>): KFunction<T> {
    if (type !in constructors) {
        constructors[type] = Value<KFunction<*>?>(type.constructors.find { it.parameters.isEmpty() })
    }
    @Suppress("UNCHECKED_CAST")
    return constructors[type]?.value as? KFunction<T>
            ?: throw NoSuchMethodException("Cannot find constructor with no parameter")
}

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
    val constructor = queryConstructor(T::class)
    val value = this.tag.getCompound(key)
    if (!this.tag.hasKeyOfType(key, TagType.TAG.id)) {
        this.tag.setCompound(key, value)
    }
    val accessor = constructor.call()
    accessor.nbtTagCompound = value
    return accessor
}

inline fun <reified T : NbtAccessor> createAccessor(tag: NBTTagCompound): T {
    val constructor = queryConstructor(T::class)
    val accessor = constructor.call()
    accessor.nbtTagCompound = tag
    return accessor
}