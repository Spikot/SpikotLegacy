package kr.heartpattern.spikot.repository

import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.MutablePropertyMap
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.repository.persistence.Storage
import kr.heartpattern.spikot.repository.persistence.StorageFactory

abstract class Repository<K, V>(
    protected val storageFactory: StorageFactory,
    protected val keySerializer: KSerializer<K>,
    protected val valueSerializer: KSerializer<V>,
    protected val namespace: String? = null
) : AbstractModule() {
    protected lateinit var persistenceManager: Storage<K, V>
    override fun onLoad(context: MutablePropertyMap) {
        super.onLoad(context)
        persistenceManager = storageFactory.createPersistenceManager(
            namespace ?: this::class.qualifiedName!!,
            this,
            keySerializer,
            valueSerializer
        )
    }
}