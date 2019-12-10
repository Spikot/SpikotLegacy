@file:Suppress("unused")

package kr.heartpattern.spikot.player

import kr.heartpattern.spikot.adapters.PlayerPropertyAdapter
import kr.heartpattern.spikot.misc.FlagProperty
import kr.heartpattern.spikot.misc.MutableFlagProperty
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.misc.Property
import org.bukkit.entity.Player
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

operator fun <T> Player.get(property: Property<T>): T? =
    if (PlayerPropertyAdapter.contains(this, property))
        PlayerPropertyAdapter.get(this, property)
    else
        null

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
operator fun <T> Player.contains(property: FlagProperty): Boolean = this[property]
fun <T> Player.remove(property: MutableProperty<T>): T? = PlayerPropertyAdapter.remove(this, property)

fun <T> Player.delegate(property: Property<T>): ReadOnlyProperty<Any?, T?> {
    return object : ReadOnlyProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, prop: KProperty<*>): T? {
            return this@delegate[property]
        }
    }
}

fun <T> Player.mutableDelegate(property: MutableProperty<T>): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, prop: KProperty<*>): T? {
            return this@mutableDelegate[property]
        }

        override fun setValue(thisRef: Any?, prop: KProperty<*>, value: T?) {
            this@mutableDelegate[property] = value
        }
    }
}