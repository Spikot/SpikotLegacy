package kr.heartpattern.spikot.persistence.storage

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.serialization.SerializeType
import org.bukkit.plugin.Plugin

/**
 * Persistence delegate. This class do actual key-value data persist.
 * @param V Type of value
 */
interface SingletonStorage<V> : Storage {
    /**
     * Load data from persistence storage.
     * @return Loaded value
     */
    suspend fun get(): Option<V>

    /**
     * Save data to persistence storage.
     * @param value Value to save
     */
    suspend fun set(value: Option<V>)
}

interface SingletonStorageFactory : StorageFactory {
    fun <V> createSingletonStorage(
        plugin: Plugin,
        namespace: String,
        serializer: KSerializer<V>,
        serializeType: SerializeType
    ): SingletonStorage<V>
}