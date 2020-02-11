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

import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.thread.runNextSync
import kr.heartpattern.spikot.utils.nonnull
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.Inventory

/**
 * Define menu inventory
 */
abstract class MenuProvider : AbstractModule() {
    /**
     * Id of menu provider used internally
     */
    val id: Int by contextDelegate(MenuIdProperty).nonnull()

    /**
     * Player who interact with this menu
     */
    val player: Player by contextDelegate(MenuPlayerProperty).nonnull()

    /**
     * Menu builder to update inventory
     */
    val menu: MenuBuilder by contextDelegate(MenuBuilderProperty).nonnull()

    /**
     * Inventory this menu provider use
     */
    val inventory: Inventory by contextDelegate(MenuInventoryProperty).nonnull()

    /**
     * Whether menu is opened.
     */
    val isOpen: Boolean by contextDelegate(MenuOpenProperty)

    private fun updateInventory() {
        for (index in 0 until player.openInventory.topInventory.size) {
            val item = menu.slot[SlotPosition(index)]
            player.openInventory.topInventory.setItem(index, item?.itemStack)
        }
        player.updateInventory()
    }

    /**
     * Update content of inventory safely.
     */
    @Suppress("unused")
    protected fun update(build: MenuBuilder.() -> Unit) {
        menu.build()
        if (isOpen) {
            spikot.runNextSync {
                updateInventory()
            }
        }
    }

    /**ㅇ
     * Invoked when menu is close.
     * @return false to prevent menu closing.
     */
    open fun onClose(): Boolean {
        return true
    }

    /**
     * Invoked when interact with this menu
     */
    open fun onInteract(event: InventoryInteractEvent) {
        event.isCancelled = true
    }

    /**
     * Invoked when drop item while this menu is opened.
     */
    open fun onDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }
}