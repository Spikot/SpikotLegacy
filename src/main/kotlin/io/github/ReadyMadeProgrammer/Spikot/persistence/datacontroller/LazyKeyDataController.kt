package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.ReadyMadeProgrammer.Spikot.misc.StringConverter
import io.github.ReadyMadeProgrammer.Spikot.persistence.gson.gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

open class LazyIntKeyDataController<V : Any>(caffeine: Caffeine<Int, V>) : LazyKeyDataController<Int, V>(StringConverter.INT, caffeine)

open class LazyStringKeyDataController<V : Any>(caffeine: Caffeine<String, V>) : LazyKeyDataController<String, V>(StringConverter.STRING, caffeine)

open class LazyUUIDKeyDataController<V : Any>(caffeine: Caffeine<UUID, V>) : LazyKeyDataController<UUID, V>(StringConverter.UUID, caffeine)

open class LazyKeyDataController<K : Any, V : Any>(
        private val keySerializer: StringConverter<K>,
        caffeine: Caffeine<K, V>
) : DataController<K, V> {
    private lateinit var root: File
    private lateinit var valueType: KClass<*>
    private val cache = caffeine.removalListener { key: Any?, value: Any?, removalCause ->
        @Suppress("UNCHECKED_CAST")
        val serializedKey = keySerializer.write(key as K)
        try {
            val file = File(root, "$serializedKey.json")
            file.createNewFile()
            val writer = FileWriter(file)
            gson.toJson(value, valueType.java, writer)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.buildAsync { key: Any? ->
        val serializedKey = keySerializer.write(key as K)
        val file = File(root, "$serializedKey.json")
        if (file.exists()) {
            val reader = FileReader(file)
            val v = gson.fromJson(reader, valueType.java)
            reader.close()
            v as V?
        } else {
            null
        }
    }

    override fun initialize(directory: File, valueType: KClass<*>) {
        root = File(directory, valueType.qualifiedName)
        if (!root.exists()) {
            root.mkdirs()
        }
        this.valueType = valueType
    }

    override fun destroy() {
        cache.synchronous()
    }

    fun get(key: K, callback: (V?) -> Unit) {
        cache.get(key).thenAccept(callback)
    }

    override operator fun get(key: K): V? {
        return cache.get(key).get()
    }

    operator fun set(key: K, value: V) {
        this[key] = CompletableFuture.completedFuture(value)
    }

    operator fun set(key: K, value: CompletableFuture<V>) {
        cache.put(key, value)
    }

    override val entries: Set<Map.Entry<K, V>> = cache.synchronous().asMap().entries

    override val keys: Set<K> = cache.synchronous().asMap().keys

    override val size: Int = cache.synchronous().asMap().size

    override val values: Collection<V> = cache.synchronous().asMap().values

    override fun containsKey(key: K): Boolean {
        return cache.synchronous().asMap().containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return cache.synchronous().asMap().containsValue(value)
    }

    override fun isEmpty(): Boolean {
        return cache.synchronous().asMap().isEmpty()
    }
}