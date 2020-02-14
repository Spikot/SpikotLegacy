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

@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.persistence.repository

import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.persistence.storage.KeyValueStorage
import kr.heartpattern.spikot.persistence.storage.SingletonStorage

internal fun <V> emptySingletonStorage(): SingletonStorage<V> = EmptySingletonStorage as SingletonStorage<V>
internal fun <K, V> emptyKeyValueStorage(): KeyValueStorage<K, V> = EmptyKeyValueStorage as KeyValueStorage<K, V>

internal object EmptySingletonStorage : AbstractModule(), SingletonStorage<Any?> {
    override suspend fun get(): Option<Any?> {
        return None
    }

    override suspend fun set(value: Option<Any?>) {

    }
}

internal object EmptyKeyValueStorage : AbstractModule(), KeyValueStorage<Any?, Any?> {
    override suspend fun getAllKeys(): Collection<Any?> {
        return emptyList()
    }

    override suspend fun save(key: Any?, value: Option<Any?>) {

    }

    override suspend fun load(key: Any?): Option<Any?> {
        return None
    }
}