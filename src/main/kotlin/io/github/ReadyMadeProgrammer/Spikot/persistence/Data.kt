package io.github.ReadyMadeProgrammer.Spikot.persistence

import io.github.ReadyMadeProgrammer.Spikot.gson.gson
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Data

@PublishedApi
internal object DataManager {
    val dataClass = HashMap<KClass<*>, Any>()
    fun initialize(dataSets: Collection<KClass<*>>) {
        dataSets.forEach { cls ->
            val file = filer(cls.qualifiedName!!)
            val text = file.readText()
            if (text.isBlank()) {
                dataClass[cls] = cls.createInstance()
            } else {
                dataClass[cls] = gson.fromJson(text, cls.java)
            }
        }
    }

    fun save() {
        dataClass.forEach { (cls, value) ->
            val file = filer(cls.qualifiedName!!)
            file.writeText(gson.toJson(value))
        }
    }

    private fun filer(name: String): File {
        val dataFolder = spikotPlugin.dataFolder
        if (!dataFolder.exists()) dataFolder.mkdir()
        val pdFolder = File(dataFolder, "persistence")
        if (!pdFolder.exists()) pdFolder.mkdir()
        val file = File(pdFolder, "$name.json")
        if (!file.exists()) file.createNewFile()
        return file
    }
}