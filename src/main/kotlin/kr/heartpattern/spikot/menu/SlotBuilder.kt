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

package kr.heartpattern.spikot.menu

import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

/**
 * Represent click information
 * @param point Position information about where is clicked
 * @param type Type of click
 */
class ClickEvent(val point: SlotPosition, val type: ClickType)

/**
 * Configure slot
 */
@MenuDsl
class SlotBuilder(val slot: SlotPosition) {
    /**
     * Item that display in slot
     */
    var display: ItemStack = ItemStack(Material.AIR)

    @Deprecated(
        "item is deprecated for duplicated name with item builder",
        ReplaceWith("display")
    )
    var item: ItemStack
        get() = display
        set(value) {
            display = value
        }
    internal val eventHandlers = HashSet<ClickHandler>()

    /**
     * Add handler which invoked when player click this slot
     * @param handler Handler to receive click event
     */
    fun handler(handler: ClickEvent.() -> Unit) {
        eventHandlers += { point: SlotPosition, type: ClickType -> ClickEvent(point, type).handler() }
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(ClickHandler)"))
    operator fun ClickHandler.unaryPlus() {
        eventHandlers += this
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(()->Unit)"))
    operator fun (() -> Unit).unaryPlus() {
        eventHandlers += { _: SlotPosition, _: ClickType -> this() }
    }
}

internal data class Slot(val itemStack: ItemStack, val clickHandler: MutableSet<ClickHandler>)