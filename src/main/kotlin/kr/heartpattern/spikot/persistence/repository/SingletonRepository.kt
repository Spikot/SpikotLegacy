/*
 * Copyright 2020 HeartPattern
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
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.BaseModule
import kr.heartpattern.spikot.module.Module
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.persistence.storage.SingletonStorageFactory
import kr.heartpattern.spikot.serialization.SerializeType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@BaseModule
@Module(priority = ModulePriority.LOWEST)
abstract class SingletonRepository<V>(
    protected val storageFactory: SingletonStorageFactory,
    protected val serializer: KSerializer<V>,
    protected val default: () -> V,
    protected val namespace: String? = null,
    protected val serializeType: SerializeType = SerializeType.JSON
) : AbstractModule() {
    protected lateinit var persistenceManager: SingletonStorage<V>
    protected var value: Option<V> = None
    override fun onEnable() {
        persistenceManager = storageFactory.createSingletonStorage(
            this.plugin,
            namespace ?: this::class.qualifiedName!!,
            serializer,
            serializeType
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