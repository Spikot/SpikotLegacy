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

/**
 * Get property for player
 * @param property Property to get
 * @return Property value if present, otherwise null.
 */
operator fun <T> Player.get(property: Property<T>): T? =
    if (PlayerPropertyAdapter.contains(this, property))
        PlayerPropertyAdapter.get(this, property)
    else
        null

/**
 * Get flag property for player
 * @param property FlagProperty to get
 * @return Property value if present, otherwise null.
 */
operator fun Player.get(property: FlagProperty): Boolean = PlayerPropertyAdapter.contains(this, property)

/**
 * Set property for player
 * @param property Property to set
 * @param value Value to set
 */
operator fun <T> Player.set(property: MutableProperty<T>, value: T?) = PlayerPropertyAdapter.set(this, property, value)

/**
 * Set flag property for player
 * @param property FlagProperty to set
 * @param value Value to set
 */
operator fun Player.set(property: MutableFlagProperty, value: Boolean) {
    if (value) {
        PlayerPropertyAdapter.set(this, property, Unit)
    } else {
        PlayerPropertyAdapter.remove(player, property)
    }
}

/**
 * Check player contains property
 * @param property Property to check
 * @return Whether player has property
 */
operator fun <T> Player.contains(property: Property<T>): Boolean = PlayerPropertyAdapter.contains(this, property)

/**
 * Remove property from player
 * @param property Property to remove
 * @return Removed value
 */
fun <T> Player.remove(property: MutableProperty<T>): T? = PlayerPropertyAdapter.remove(this, property)

/**
 * Delegate of player property
 * @param prop Property to delegate
 * @return Property delegate
 */
fun <T> Player.delegate(prop: Property<T>): ReadOnlyProperty<Any?, T?> {
    return object : ReadOnlyProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return this@delegate[prop]
        }
    }
}

/**
 * Delegate of player property
 * @param prop MutableProperty to delegate
 * @return Property delegate
 */
fun <T> Player.mutableDelegate(prop: MutableProperty<T>): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return this@mutableDelegate[prop]
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            this@mutableDelegate[prop] = value
        }
    }
}