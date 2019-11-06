@file:Suppress("unused")

package kr.heartpattern.spikot.utils

import kr.heartpattern.spikot.adapters.PlayerPropertyAdapter
import kr.heartpattern.spikot.misc.FlagProperty
import kr.heartpattern.spikot.misc.MutableFlagProperty
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.misc.Property
import org.bukkit.entity.Player

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