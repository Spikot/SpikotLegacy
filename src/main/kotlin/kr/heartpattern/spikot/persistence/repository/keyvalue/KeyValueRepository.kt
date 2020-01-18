package kr.heartpattern.spikot.persistence.repository.keyvalue

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class KeyValueRepository<K : Any, V : Any>(
    storageFactory: KeyValueStorageFactory,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    protected val storage: MutableMap<K, V> = HashMap(),
    namespace: String? = null
) : AbstractKeyValueRepository<K, V>(
    storageFactory,
    keySerializer,
    valueSerializer,
    namespace
), MutableMap<K, V> by storage {
    override fun onEnable() {
        runBlocking {
            storage.putAll(
                persistenceManager.loadAll(persistenceManager.getAllKeys())
                    .map { (key, value) ->
                        key to value.getOrNull()!!
                    }
            )
        }
    }

    override fun onDisable() {
        runBlocking {
            val new = storage.keys
            val old = persistenceManager.getAllKeys()
            persistenceManager.saveAll(
                storage.mapValues { it.value.option }
            )

            persistenceManager.saveAll((old - new).map { it to None }.toMap())
        }
    }
}