package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import io.github.ReadyMadeProgrammer.Spikot.misc.StringConverter
import io.github.ReadyMadeProgrammer.Spikot.persistence.gson.gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.reflect.KClass

open class IntKeyDataController<V : Any> : KeyDataController<Int, V>(StringConverter.INT)

open class StringKeyDataController<V : Any> : KeyDataController<String, V>(StringConverter.STRING)

open class UUIDKeyDataController<V : Any> : KeyDataController<UUID, V>(StringConverter.UUID)

open class KeyDataController<K : Any, V : Any>(protected val keySerializer: StringConverter<K>) : DataController<K, V>, MutableMap<K, V> {
    protected lateinit var root: File
    protected lateinit var valueType: KClass<*>
    protected val value = HashMap<K, V>()
    override fun initialize(directory: File, valueType: KClass<*>) {
        root = File(directory, valueType.qualifiedName)
        if (!root.exists()) {
            root.mkdirs()
        }
        this.valueType = valueType
        root.listFiles { _, name -> name.endsWith(".json") }
                .forEach {
                    try {
                        val key = keySerializer.read(it.name.substring(0 until (it.name.length - 5)))
                        val reader = FileReader(it)
                        val value = gson.fromJson(reader, valueType.java)
                        reader.close()
                        this.value[key] = value as V
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
    }

    override fun destroy() {
        root.listFiles().forEach { it.deleteRecursively() }
        value.forEach { (key, value) ->
            val file = File(root, "${keySerializer.write(key)}.json")
            try {
                file.createNewFile()
                val writer = FileWriter(file)
                gson.toJson(value, valueType.java, writer)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
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