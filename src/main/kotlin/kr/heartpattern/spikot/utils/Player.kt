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

package kr.heartpattern.spikot.utils

import kr.heartpattern.spikot.adapters.PlayerPropertyAdapter
import kr.heartpattern.spikot.misc.FlagProperty
import kr.heartpattern.spikot.misc.MutableFlagProperty
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.misc.Property
import org.bukkit.entity.Player
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

operator fun <T> Player.get(property: Property<T>): T? = PlayerPropertyAdapter.get(this, property)
operator fun Player.get(property: FlagProperty): Boolean = PlayerPropertyAdapter.contains(this, property)
operator fun <T> Player.set(property: MutableProperty<T>, value: T?) = PlayerPropertyAdapter.set(this, property, value)
operator fun Player.set(property: MutableFlagProperty, value: Boolean) {
    if (value) {
        PlayerPropertyAdapter.set(this, property, Unit)
    } else {
        PlayerPropertyAdapter.remove(player, property)
    }
}

operator fun <T> Player.contains(property: Property<T>): Boolean = PlayerPropertyAdapter.contains(this, property)
fun <T> Player.remove(property: MutableProperty<T>): T? = PlayerPropertyAdapter.remove(this, property)

fun <T> playerDelegate(prop: Property<T>): ReadOnlyProperty<Player, T?> {
    return object : ReadOnlyProperty<Player, T?> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T? {
            return thisRef[prop]
        }
    }
}

fun <T> playerDelegate(prop: MutableProperty<T>): ReadWriteProperty<Player, T?> {
    return object : ReadWriteProperty<Player, T?> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T? {
            return thisRef[prop]
        }

        override fun setValue(thisRef: Player, property: KProperty<*>, value: T?) {
            thisRef[prop] = value
        }
    }
}

fun playerDelegate(prop: FlagProperty): ReadOnlyProperty<Player, Boolean> {
    return object : ReadOnlyProperty<Player, Boolean> {
        override fun getValue(thisRef: Player, property: KProperty<*>): Boolean {
            return thisRef[prop]
        }
    }
}

fun playerDelegate(prop: MutableFlagProperty): ReadWriteProperty<Player, Boolean> {
    return object : ReadWriteProperty<Player, Boolean> {
        override fun getValue(thisRef: Player, property: KProperty<*>): Boolean {
            return thisRef[prop]
        }

        override fun setValue(thisRef: Player, property: KProperty<*>, value: Boolean) {
            thisRef[prop] = value
        }
    }
}