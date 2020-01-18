package kr.heartpattern.spikot.persistence.repository.keyvalue

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.persistence.repository.Repository
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.KeyValueStorageFactory

abstract class AbstractKeyValueRepository<K, V>(
    protected val storageFactory: KeyValueStorageFactory,
    protected val keySerializer: KSerializer<K>,
    protected val valueSerializer: KSerializer<V>,
    protected val namespace: String? = null
) : AbstractModule(), Repository {
    protected lateinit var persistenceManager: KeyValueStorage<K, V>
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        persistenceManager = storageFactory.createKeyValueStorage(
            this.plugin,
            namespace ?: this::class.qualifiedName!!,
            keySerializer,
            valueSerializer
        )
    }
}