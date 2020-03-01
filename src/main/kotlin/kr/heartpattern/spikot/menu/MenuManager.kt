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

@file:Suppress("unused")

package kr.heartpattern.spikot.menu

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.misc.MutableFlagProperty
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.module.*
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.thread.runNextSync
import kr.heartpattern.spikot.utils.attachInvisible
import kr.heartpattern.spikot.utils.findInvisible
import kr.heartpattern.spikot.utils.hasInvisible
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal object MenuIdProperty : MutableProperty<Int>
internal object MenuPlayerProperty : MutableProperty<Player>
internal object MenuBuilderProperty : MutableProperty<MenuBuilder>
internal object MenuInventoryProperty : MutableProperty<Inventory>
internal object MenuOpenProperty : MutableFlagProperty

/**
 * Open inventory safely. Can be used in inventory event.
 * @receiver Player to open inventory
 * @param inventory Inventory to open
 */
fun Player.safeOpenInventory(inventory: Inventory) {
    spikot.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.openInventory(inventory)
    }
}

/**
 * Close inventory safely. Can be used in inventory event
 * @receiver Player to close inventory
 */
fun Player.safeCloseInventory() {
    spikot.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.closeInventory()
    }
}

/**
 * Open inventory with given menu provider and inventory
 * @receiver Player to open inventory
 * @param plugin Plugin to handle menu
 * @param menuProvider MenuProvider which handle menu
 * @param inventory Inventory to use for displaying menu
 */
@JvmOverloads
fun Player.openInventory(plugin: SpikotPlugin, menuProvider: MenuProvider, inventory: Inventory? = null) {
    val handler = ModuleManager.createModule(menuProvider, plugin)
    val builder = MenuBuilder()
    handler.context[MenuIdProperty] = MenuManager.id
    handler.context[MenuPlayerProperty] = this
    handler.context[MenuBuilderProperty] = builder
    handler.context[MenuOpenProperty] = false
    MenuManager.openedInventory[menuProvider.id] = handler
    handler.load()
    handler.enable()

    val inv = inventory
        ?: Bukkit.createInventory(null, builder.size, builder.title.attachInvisible(menuProvider.id))

    handler.context[MenuInventoryProperty] = inv
    val contents = Array(builder.size) { ItemStack(Material.AIR) }
    builder.slot.forEach { (point, item) ->
        contents[point.x + point.y * 9] = item.itemStack
    }
    inv.contents = contents
    handler.context[MenuOpenProperty] = true
    player.safeOpenInventory(inv)
}

/**
 * Get MenuProvider this player currently open. Return null if player does not open MenuProvider
 * @receiver Player to get current MenuProvider
 * @param T Type of MenuProvider
 */
inline fun <reified T : MenuProvider> Player.getOpenedInventory(): T? {
    val title = player.openInventory.title
    if (title.isNullOrBlank() || !title.hasInvisible()) return null
    return MenuManager.openedInventory[title.findInvisible()] as? T
}

@ServerModule(ModulePriority.API)
@PublishedApi
internal object MenuManager : AbstractModule() {
    internal var id: Int = 0
        get() {
            field++
            return field
        }
        private set

    @PublishedApi
    internal val openedInventory = mutableMapOf<Int, ModuleHandler>()

    @EventHandler
    fun onSlotClick(event: InventoryClickEvent) {
        val title = event.whoClicked.openInventory.title
        if (title == null || !title.hasInvisible()) {
            return
        }
        val inventoryId = title.findInvisible()
        val provider = openedInventory[inventoryId]?.module as MenuProvider? ?: return
        provider.onInteract(event)
        if (event.view.topInventory != null && event.rawSlot < event.view.topInventory.size) {
            val slotId = event.slot
            val x = slotId % 9
            val y = slotId / 9
            val point = SlotPosition(x, y)
            val slot = provider.menu.slot[point] ?: return
            slot.clickHandler.forEach {
                it(point, event.click)
            }
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        val title = event.whoClicked.openInventory.title
        if (title.hasInvisible()) {
            val inventoryId = title.findInvisible()
            val provider = openedInventory[inventoryId]?.module as MenuProvider? ?: return
            provider.onInteract(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (!event.view.title.hasInvisible()) {
            return
        }
        val inventoryId = event.view.title.findInvisible()
        val handler = openedInventory[inventoryId] ?: return
        val provider = handler.module as MenuProvider
        if (!provider.onClose()) {
            (event.player as Player).safeOpenInventory(provider.inventory)
        } else {
            openedInventory.remove(inventoryId)
            handler.context[MenuOpenProperty] = false
            handler.disable()
        }
    }
}