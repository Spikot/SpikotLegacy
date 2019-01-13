package io.github.ReadyMadeProgrammer.Spikot.i18n

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import java.io.File
import java.util.*
import kotlin.reflect.KClass

val gson = GsonBuilder().setPrettyPrinting().create()

internal fun loadKey(enum: KClass<*>) {
    val values = enum.java.enumConstants
    val file = File(spikotPlugin.dataFolder.parentFile, "message/${(values[0] as MessageKey).root}.json")
    if (!file.exists()) {
        write(values)
    }
    loadMessage(file, (values[0] as MessageKey).root)
}

private fun write(values: Array<*>) {
    val folder = File(spikotPlugin.dataFolder.parentFile, "message")
    if (!folder.exists()) folder.mkdir()
    val file = File(folder, "${(values[0] as MessageKey).root}.json")
    val descFile = File(folder, "${(values[0] as MessageKey).root}-desc.json")
    val json = JsonObject()
    val desc = JsonObject()
    for (i in values) {
        json.addProperty((i as Enum<*>).name, (i as MessageKey).default)
        desc.addProperty(i.name, i.description)
    }
    val stack = Stack<File>()
    var ff = file.parentFile
    println("W")
    while (ff != folder) {
        println(ff)
        stack.push(ff)
        ff = ff.parentFile
    }
    while (stack.isNotEmpty()) {
        val pop = stack.pop()
        if (!pop.exists()) {
            pop.mkdir()
            println(pop)
        }
    }
    file.createNewFile()
    descFile.createNewFile()
    file.writeText(gson.toJson(json))
    descFile.writeText(gson.toJson(json))
}

private fun loadMessage(file: File, root: String) {
    val json = JsonParser().parse(file.readText()).asJsonObject
    val map = HashMap<String, String>()
    for ((key, value) in json.entrySet()) {
        map[key] = value.asJsonPrimitive.asString
    }
    messageMap[root] = map
}