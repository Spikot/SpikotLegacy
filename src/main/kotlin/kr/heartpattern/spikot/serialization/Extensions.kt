@file:Suppress("UNCHECKED_CAST")

package kr.heartpattern.spikot.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kotlinx.serialization.map
import kotlinx.serialization.set

val <T> KSerializer<T>.mutableList: KSerializer<MutableList<T>>
    get() = list as KSerializer<MutableList<T>>

val <T> KSerializer<T>.mutableSet: KSerializer<MutableSet<T>>
    get() = set as KSerializer<MutableSet<T>>

val <K, V> Pair<KSerializer<K>, KSerializer<V>>.mutableMap: KSerializer<MutableMap<K, V>>
    get() = map as KSerializer<MutableMap<K, V>>