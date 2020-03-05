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

package kr.heartpattern.spikot.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.set

val <T> KSerializer<T>.mutableList: KSerializer<MutableList<T>>
    get() = list as KSerializer<MutableList<T>>

val <T> KSerializer<T>.mutableSet: KSerializer<MutableSet<T>>
    get() = set as KSerializer<MutableSet<T>>

val <K, V> Pair<KSerializer<K>, KSerializer<V>>.mutableMap: KSerializer<MutableMap<K, V>>
    get() = MapSerializer(first, second) as KSerializer<MutableMap<K, V>>