package kr.heartpattern.spikot.file

import kr.heartpattern.spikot.gson.gson
import kr.heartpattern.spikot.misc.MutablePropertyMap
import net.jodah.typetools.TypeResolver
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
open class SingletonFileDataController<V : Any>(private val constructor: () -> V) : FileDataController() {
    protected lateinit var value: V
    private val valueType: KClass<V> = TypeResolver.resolveRawArgument(NullablePlayerFileDataController::class.java, this::class.java).kotlin as KClass<V>

    constructor(type: KClass<V>) : this({ type.createInstance() })

    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        try {
            val file = File(directory, "data.json")
            FileReader(file).use { reader ->
                value = gson.fromJson(reader, valueType.java) ?: constructor()
            }
        } catch (e: Exception) {
            logger.error(e) { "Error occur while read singleton data" }
        }
    }

    override fun onDisable() {
        try {
            val file = File(directory, "data.json")
            FileWriter(file).use { writer ->
                gson.toJson(value, valueType.java, writer)
            }
        } catch (e: Exception) {
            logger.error(e) { "Error occur while read " }
        }
    }

    fun get(): V {
        return value
    }

    fun set(value: V) {
        this.value = value
    }
}
