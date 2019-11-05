@file:Suppress("unused")

package kr.heartpattern.spikot.chat

import com.github.salomonbrys.kotson.set
import com.google.gson.JsonObject
import kr.heartpattern.spikot.adapters.NBTAdapter
import kr.heartpattern.spikot.chat.HoverEventType.*
import kr.heartpattern.spikot.nbt.getWrappedTag
import kr.heartpattern.spikot.nbt.toCraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

fun ChatBuilder.showText(hoverBuilder: ChatBuilder, builder: Builder) {
    hover(SHOW_TEXT, hoverBuilder, builder)
}

fun ChatBuilder.showText(hoverText: String, builder: Builder) {
    hover(SHOW_TEXT, chat(hoverText), builder)
}

fun ChatBuilder.showText(hoverBuilder: ChatBuilder, text: String) {
    hover(SHOW_TEXT, hoverBuilder, text)
}

fun ChatBuilder.showText(hoverText: String, text: String) {
    hover(SHOW_TEXT, chat(hoverText), text)
}

fun showText(hoverBuilder: ChatBuilder, builder: Builder): ChatBuilder {
    return hover(SHOW_TEXT, hoverBuilder, builder)
}

fun showText(hoverText: String, builder: Builder): ChatBuilder {
    return hover(SHOW_TEXT, chat(hoverText), builder)
}

fun showText(hoverBuilder: ChatBuilder, text: String): ChatBuilder {
    return hover(SHOW_TEXT, hoverBuilder, text)
}

fun showText(hoverText: String, text: String): ChatBuilder {
    return hover(SHOW_TEXT, chat(hoverText), text)
}

fun ChatBuilder.showItem(item: ItemStack, builder: Builder) {
    hover(SHOW_ITEM, chat(item.toJson()), builder)
}

fun ChatBuilder.showItem(item: ItemStack, text: String) {
    hover(SHOW_ITEM, chat(item.toJson()), text)
}

fun showItem(item: ItemStack, builder: Builder): ChatBuilder {
    return hover(SHOW_ITEM, chat(item.toJson()), builder)
}

fun showItem(item: ItemStack, text: String): ChatBuilder {
    return hover(SHOW_ITEM, chat(item.toJson()), text)
}

fun ChatBuilder.showEntity(entity: Entity, builder: Builder) {
    hover(SHOW_ENTITY, chat(entity.toJson()), builder)
}

fun ChatBuilder.showEntity(entity: Entity, text: String) {
    hover(SHOW_ENTITY, chat(entity.toJson()), text)
}

fun showEntity(entity: Entity, builder: Builder): ChatBuilder {
    return hover(SHOW_ENTITY, chat(entity.toJson()), builder)
}

fun showEntity(entity: Entity, text: String): ChatBuilder {
    return hover(SHOW_ENTITY, chat(entity.toJson()), text)
}

private fun Entity.toJson(): String {
    val json = JsonObject()
    json["id"] = this.uniqueId.toString()
    json["type"] = "minecraft:${type.name.toLowerCase()}"
    if (customName != null) {
        json["name"] = customName
    }
    return json.toString()
}

private fun ItemStack.toJson(): String {
    return NBTAdapter.compressNBT(toCraftItemStack().getWrappedTag()!!).toString()
}