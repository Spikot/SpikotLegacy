package kr.heartpattern.spikot.repository

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.UnitSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.repository.persistence.StorageFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class SingletonRepository<V : Any>(
    storageFactory: StorageFactory,
    serializer: KSerializer<V>,
    protected val default: () -> V,
    namespace: String?
) : Repository<Unit, V>(
    storageFactory,
    UnitSerializer,
    serializer,
    namespace
) {
    protected lateinit var value: V
    override fun onEnable() {
        runBlocking {
            value = when (val result = persistenceManager.load(Unit)) {
                is Just -> result.value
                is None -> {
                    default()
                }
            }
        }
    }

    override fun onDisable() {
        runBlocking {
            persistenceManager.save(Unit, Just(value))
        }
    }

    fun get(): V {
        return value
    }

    fun set(value: V) {
        this.value = value
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