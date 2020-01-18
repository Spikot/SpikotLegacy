package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.persistence.storage.SingletonStorageFactory
import org.bukkit.plugin.Plugin
import java.io.File

object FileStorageFactory : KeyValueStorageFactory, SingletonStorageFactory {
    override fun <K, V> createKeyValueStorage(
        plugin: Plugin,
        namespace: String,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>
    ): KeyValueStorage<K, V> {
        return FileKeyValueStorage(File(plugin.dataFolder, namespace), keySerializer, valueSerializer)
    }

    override fun <V> createSingletonStorage(
        plugin: Plugin,
        namespace: String,
        serializer: KSerializer<V>
    ): SingletonStorage<V> {
        return FileSingletonStorage(File(plugin.dataFolder, "${namespace}.json"), serializer)
    }
}