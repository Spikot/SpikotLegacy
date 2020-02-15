/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.persistence.repository

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
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
    storage: SingletonStorage<V>,
    protected val default: () -> V
) : AbstractRepository<SingletonStorage<V>>() {
    final override var storage: SingletonStorage<V> = storage
        private set

    protected var value: Option<V> = None

    @Deprecated("Create storage directly")
    constructor(
        storageFactory: SingletonStorageFactory,
        serializer: KSerializer<V>,
        default: () -> V,
        namespace: String? = null
    ) : this(emptySingletonStorage(), default) {
        this.storage = storageFactory.createSingletonStorage(namespace ?: this::class.qualifiedName!!, serializer)
    }

    override fun onEnable() {
        runBlocking {
            value = when (val result = storage.get()) {
                is Just -> result
                is None -> Just(default())
            }
        }
        super.onEnable()
    }

    override fun onDisable() {
        runBlocking {
            storage.set(value)
        }
        super.onDisable()
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