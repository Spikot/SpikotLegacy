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

package kr.heartpattern.spikot.adapters

import kr.heartpattern.spikot.adapter.IAdapter
import kr.heartpattern.spikot.adapter.VersionAdapterResolver
import kr.heartpattern.spikot.adapter.VersionType
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.misc.Property
import kr.heartpattern.spikot.module.Module
import org.bukkit.entity.Player

/**
 * Adapter to handle player property. Some Spikot friendly bukkit support efficient ways to handle it.
 */
interface PlayerPropertyAdapter : IAdapter {
    /**
     * Get player property
     * @param player Player to get property
     * @param property Property to get
     * @return Property value for player
     * @throws NullPointerException If player does not have property
     */
    @Throws(NullPointerException::class)
    fun <T> get(player: Player, property: Property<T>): T

    /**
     * Set player property
     * @param player Player to set property
     * @param property Property to set
     * @param value Value to set
     */
    fun <T> set(player: Player, property: MutableProperty<T>, value: T?)

    /**
     * Check player has property
     * @param player Player to check
     * @param property Property to check
     * @return Whether player has property
     */
    fun contains(player: Player, property: Property<*>): Boolean

    /**
     * Remove property from player
     * @param player Player to remove property
     * @param property Property to be removed
     * @return Removed value
     */
    fun <T> remove(player: Player, property: MutableProperty<T>): T?

    @Module
    object Resolver : VersionAdapterResolver<PlayerPropertyAdapter>(PlayerPropertyAdapter::class, VersionType.BUKKIT)

    companion object : PlayerPropertyAdapter by Resolver.default
}