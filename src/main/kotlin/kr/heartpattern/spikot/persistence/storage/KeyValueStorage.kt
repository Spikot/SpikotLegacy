package kr.heartpattern.spikot.persistence.storage

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import org.bukkit.plugin.Plugin

/**
 * Persistence delegate. This class do actual key-value data persist.
 * @param K Key type
 * @param V Value type
 */
interface KeyValueStorage<K, V> : Storage {
    /**
     * All keys
     * @return Collection of all key
     */
    suspend fun getAllKeys(): Collection<K>

    /**
     * Save key-value pair into persistence storage.
     * @param key Key
     * @param value [kr.heartpattern.spikot.misc.Just] to save data, [kr.heartpattern.spikot.misc.None] to delete data.
     */
    suspend fun save(key: K, value: Option<V>)

    /**
     * Load value associate with given [key] from persistence storage.
     * @param key Key
     * @return [kr.heartpattern.spikot.misc.Just] if key is exists,
     * [kr.heartpattern.spikot.misc.None] if key does not exists.
     */
    suspend fun load(key: K): Option<V>

    /**
     * Save all key-value pair into persistence storage.
     * @param map Key-Value map to persist into storage
     */
    suspend fun saveAll(map: Map<K, Option<V>>) {
        for ((key, value) in map) {
            save(key, value)
        }
    }

    /**
     * Load all value associate with given [key] from persistence storage.
     * @param keys Collection of keys to load
     * @return Key-Value map. [kr.heartpattern.spikot.misc.Just] if key is exists,
     * [kr.heartpattern.spikot.misc.None] if key does not exists.
     */
    suspend fun loadAll(keys: Collection<K>): Map<K, Option<V>> {
        val result = HashMap<K, Option<V>>()
        for (key in keys) {
            result[key] = load(key)
        }
        return result
    }

    /**
     * Remove all key-value in persistence storage.
     */
    suspend fun clear() {
        val keys = getAllKeys()
        saveAll(keys.map { it to None }.toMap())
    }
}

interface KeyValueStorageFactory : StorageFactory {
    fun <K, V> createKeyValueStorage(
        plugin: Plugin,
        namespace: String,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>
    ): KeyValueStorage<K, V>
}