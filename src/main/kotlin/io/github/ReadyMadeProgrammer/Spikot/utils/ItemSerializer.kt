@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.utils

import com.google.gson.*
import io.github.ReadyMadeProgrammer.Spikot.reflections.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type
import java.util.*

private val classNBTCompressStreamTools = ReflectionUtils.getNmsClass("NBTCompressedStreamTools")
private val methodToByte = classNBTCompressStreamTools.getDeclaredMethod("a", classNbtTagCompound, OutputStream::class.java)
private val methodFromByte = classNBTCompressStreamTools.getDeclaredMethod("a", InputStream::class.java)
private val classItem = ReflectionUtils.getNmsClass("Item")

class NBTItemSerializer : JsonSerializer<ItemStack> {
    override fun serialize(item: ItemStack, type: Type?, ctx: JsonSerializationContext?): JsonElement {
        val nbt = item.toNmsCopy().tag
        val byteArrayOutputStream = ByteArrayOutputStream()
        methodToByte.invoke(null, nbt.handle, byteArrayOutputStream)
        val string = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
        return JsonPrimitive(string)
    }
}

class NBTItemDeserializer : JsonDeserializer<ItemStack> {
    override fun deserialize(json: JsonElement, type: Type?, ctx: JsonDeserializationContext?): ItemStack {
        val byteArray = Base64.getDecoder().decode(json.asString)
        val tag = methodFromByte.invoke(null, ByteArrayInputStream(byteArray))
        val itemStack = NmsItemStack(classNmsItemStack.getDeclaredConstructor(classItem).newInstance(null))
        itemStack.tag = NmsNbtTagCompound(tag)
        return itemStack.toBukkitCopy()
    }
}

class NBTItemInstanceCreator : InstanceCreator<ItemStack> {
    override fun createInstance(p0: Type?): ItemStack {
        return ItemStack(Material.AIR)
    }
}