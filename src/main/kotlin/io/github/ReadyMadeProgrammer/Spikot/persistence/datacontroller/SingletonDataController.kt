package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import io.github.ReadyMadeProgrammer.Spikot.persistence.gson.gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class SingletonDataController<V : Any>(private val constructor: () -> V) : DataController<Unit, V> {
    protected lateinit var value: V
    protected lateinit var file: File
    protected lateinit var valueType: KClass<*>

    constructor(type: KClass<V>) : this({ type.createInstance() })

    final override fun initialize(directory: File, valueType: KClass<*>) {
        file = File(directory, valueType.qualifiedName)
        try {
            file.createNewFile()
            val reader = FileReader(file)
            @Suppress("UNCHECKED_CAST")
            value = gson.fromJson(reader, valueType.java) as V? ?: constructor()
            reader.close()
            this.valueType = valueType
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    final override fun destroy() {
        try {
            val writer = FileWriter(file)
            gson.toJson(value, valueType.java, writer)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun get(): V {
        return value
    }

    fun set(value: V) {
        this.value = value
    }
}
