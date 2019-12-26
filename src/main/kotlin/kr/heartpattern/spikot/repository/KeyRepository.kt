package kr.heartpattern.spikot.repository

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.repository.persistence.StorageFactory

abstract class KeyRepository<K : Any, V : Any>(
    storageFactory: StorageFactory,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    protected val storage: MutableMap<K, V> = HashMap(),
    namespace: String? = null
) : Repository<K, V>(
    storageFactory,
    keySerializer,
    valueSerializer,
    namespace
), MutableMap<K, V> by storage {
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        runBlocking {
            storage.putAll(
                persistenceManager.loadAll(keys)
                    .map { (key, value) ->
                        key to value.getOrNull()!!
                    }
            )
        }
    }

    override fun onDisable() {
        runBlocking {
            persistenceManager.clear()
            persistenceManager.saveAll(
                storage.mapValues { it.value.option }
            )
        }
    }
}