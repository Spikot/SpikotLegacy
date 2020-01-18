package kr.heartpattern.spikot.persistence

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.persistence.storage.SingletonStorageFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class SingletonRepository<V>(
    protected val storageFactory: SingletonStorageFactory,
    protected val serializer: KSerializer<V>,
    protected val default: () -> V,
    protected val namespace: String? = null
) : AbstractModule() {
    protected lateinit var persistenceManager: SingletonStorage<V>
    protected var value: Option<V> = None
    override fun onEnable() {
        persistenceManager = storageFactory.createSingletonStorage(
            this.plugin,
            namespace ?: this::class.qualifiedName!!,
            serializer
        )
        runBlocking {
            value = when (val result = persistenceManager.get()) {
                is Just -> result
                is None -> Just(default())
            }
        }
    }

    override fun onDisable() {
        runBlocking {
            persistenceManager.set(value)
        }
    }

    fun get(): V {
        return (value as Just).value
    }

    fun set(value: V) {
        this.value = Just(value)
    }

    val delegate: ReadWriteProperty<Any, V>
        get() = object : ReadWriteProperty<Any, V> {
            override fun getValue(thisRef: Any, property: KProperty<*>): V {
                return get()
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
                set(value)
            }
        }
}