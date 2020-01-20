package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.persistence.storage.*
import kr.heartpattern.spikot.serialization.SerializeType
import org.bukkit.plugin.Plugin
import java.io.File

object FileStorageFactory : KeyValueStorageFactory, SingletonStorageFactory {
    override fun <K, V> createKeyValueStorage(
        plugin: Plugin,
        namespace: String,
        keySerializer: KSerializer<K>,
        valueSerializer: KSerializer<V>,
        serializeType: SerializeType): KeyValueStorage<K, V> {
        return FileKeyValueStorage(File(plugin.dataFolder, namespace), keySerializer, valueSerializer, serializeType)
    }

    override fun <V> createSingletonStorage(
        plugin: Plugin,
        namespace: String,
        serializer: KSerializer<V>,
        serializeType: SerializeType
    ): SingletonStorage<V> {
        return FileSingletonStorage(File(plugin.dataFolder, "${namespace}.json"), serializer, serializeType)
    }
}

