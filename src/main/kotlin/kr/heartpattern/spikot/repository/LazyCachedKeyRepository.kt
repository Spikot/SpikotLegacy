package kr.heartpattern.spikot.repository

import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import com.google.common.cache.RemovalNotification
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.misc.getOrNull
import kr.heartpattern.spikot.misc.option
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.repository.persistence.StorageFactory
import java.util.concurrent.TimeUnit

@BaseModule
@Module(priority = ModulePriority.LOWEST)
class LazyCachedKeyRepository<K, V : Any>(
    storageFactory: StorageFactory,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    cacheBuilder: CacheBuilder<Any, Any> = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES),
    namespace: String?
) : Repository<K, V>(
    storageFactory,
    keySerializer,
    valueSerializer,
    namespace
) {
    protected val cache = cacheBuilder.removalListener { notification: RemovalNotification<K, Option<V>> ->
        if (notification.key != null && notification.value != null && notification.cause != RemovalCause.REPLACED) {
            plugin.launch {
                persistenceManager.save(notification.key, notification.value)
            }
        }
    }.build<K, Option<V>>()

    override fun onDisable() {
        cache.invalidateAll()
    }

    suspend fun get(key: K): V? {
        val cached = cache.getIfPresent(key)
        return if (cached != null) {
            cached.getOrNull()
        } else {
            val loaded = persistenceManager.load(key).getOrNull()
            if (loaded != null) {
                cache.put(key, loaded.option)
            }
            loaded
        }
    }

    fun set(key: K, value: V?) {
        cache.put(key, value.option)
    }
}