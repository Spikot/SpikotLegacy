package io.github.ReadyMadeProgrammer.Spikot.gson

import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import io.github.ReadyMadeProgrammer.Spikot.nbt.nmsItemStackSaveMethod
import io.github.ReadyMadeProgrammer.Spikot.reflections.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type
import java.util.*

class ItemGsonSerializer : GsonSerializer<ItemStack> {
    companion object {
        private val classNmsNBTCompressedStreamTools = ReflectionUtils.getNmsClass("NBTCompressedStreamTools")
        private val methodNmsNBTCompressedStreamToolsWrite = classNmsNBTCompressedStreamTools.getDeclaredMethod("a", classNbtTagCompound, OutputStream::class.java)
        private val methodNmsNBTCompressedStreamToolsRead = classNmsNBTCompressedStreamTools.getDeclaredMethod("a", InputStream::class.java)
        private val constructorNmsItemStackFromNBT = classNmsItemStack.getDeclaredConstructor(classNbtTagCompound)
    }

    override fun serialize(itemStack: ItemStack, type: Type?, ctx: JsonSerializationContext?): JsonElement {
        val nms = methodAsNMSCopy(null, itemStack)
        val nbt = nmsItemStackSaveMethod(nms)
        val outputStream = ByteArrayOutputStream()
        methodNmsNBTCompressedStreamToolsWrite(null, nbt, outputStream)
        val string = Base64.getEncoder().encodeToString(outputStream.toByteArray())
        return string.toJson()
    }

    override fun deserialize(element: JsonElement, type: Type?, ctx: JsonDeserializationContext?): ItemStack {
        val byteArray = Base64.getDecoder().decode(element.asString)
        val nbt = methodNmsNBTCompressedStreamToolsRead(null, ByteArrayInputStream(byteArray))
        return methodAsBukkitCopy(constructorNmsItemStackFromNBT.newInstance(nbt)) as ItemStack
    }

    override fun createInstance(type: Type?): ItemStack {
        return ItemStack(Material.AIR)
    }

}