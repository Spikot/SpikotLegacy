package kr.heartpattern.spikot.file

import kr.heartpattern.spikot.gson.gson
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.misc.StringConverter
import kr.heartpattern.spikot.utils.catchAll
import net.jodah.typetools.TypeResolver
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass

open class IntKeyFileDataController<V : Any> : KeyFileDataController<Int, V>(StringConverter.INT)

open class StringKeyFileDataController<V : Any> : KeyFileDataController<String, V>(StringConverter.STRING)

open class UUIDKeyFileDataController<V : Any> : KeyFileDataController<UUID, V>(StringConverter.UUID)

@Suppress("UNCHECKED_CAST")
open class KeyFileDataController<K : Any, V : Any>(protected val keySerializer: StringConverter<K>) : FileDataController(), MutableMap<K, V> {
    private val keyType: KClass<K>
    private val valueType: KClass<V>
    protected val value = HashMap<K, V>()

    init {
        val generic = TypeResolver.resolveRawArguments(KeyFileDataController::class.java, this::class.java)
        keyType = generic[0].kotlin as KClass<K>
        valueType = generic[1].kotlin as KClass<V>
    }

    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        directory.listFiles { _, name -> name.endsWith(".json") }!!
            .forEach { file ->
                logger.catchAll("Cannot load key data file: $file") {
                    val key = keySerializer.read(file.name.substring(0 until file.name.length - 5))
                    val reader = FileReader(file)
                    val value = gson.fromJson(reader, valueType.java)
                    reader.close()
                    this.value[key] = value as V
                }
            }
    }

    override fun onDisable() {
        directory.listFiles()!!.forEach { it.deleteRecursively() }
        value.forEach { (key, value) ->
            val file = File(directory, "${keySerializer.write(key)}.json")
            logger.catchAll("Cannot save key data file: $key") {
                file.createNewFile()
                val writer = FileWriter(file)
                gson.toJson(value, valueType.java, writer)
                writer.flush()
                writer.close()
            }
        }
    }

    override operator fun get(key: K): V? = this.value[key]

    operator fun set(key: K, value: V?) {
        if (value == null) {
            this.value.remove(key)
        } else {
            this.value[key] = value
        }
    }

    override fun containsKey(key: K): Boolean = key in value

    override fun containsValue(value: V): Boolean = this.value.containsValue(value)

    override fun isEmpty(): Boolean = this.value.isEmpty()

    override val size: Int
        get() = value.size
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = value.entries
    override val keys: MutableSet<K>
        get() = value.keys
    override val values: MutableCollection<V>
        get() = value.values

    override fun clear() {
        value.clear()
    }

    override fun put(key: K, value: V): V? {
        return this.value.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        value.putAll(from)
    }

    override fun remove(key: K): V? {
        return value.remove(key)
    }
}