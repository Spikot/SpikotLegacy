@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.menu

import io.github.ReadyMadeProgrammer.Spikot.event.TickEvent
import io.github.ReadyMadeProgrammer.Spikot.module.AbstractModule
import io.github.ReadyMadeProgrammer.Spikot.module.LoadOrder
import io.github.ReadyMadeProgrammer.Spikot.module.Module
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import io.github.ReadyMadeProgrammer.Spikot.thread.runNextSync
import io.github.ReadyMadeProgrammer.Spikot.utils.findInvisible
import io.github.ReadyMadeProgrammer.Spikot.utils.hasInvisible
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

fun Player.safeOpenInventory(inventory: Inventory) {
    spikotPlugin.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.openInventory(inventory)
    }
}

fun Player.safeCloseInventory() {
    spikotPlugin.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.closeInventory()
    }
}

fun Player.openInventory(menuProvider: MenuProvider) {
    menuProvider.initialize(MenuManager.id, this, MenuBuilder())
    MenuManager.openedInventory[menuProvider.id] = menuProvider
    menuProvider.onOpen()
    menuProvider.openInventory()
}

val Inventory.menuProvider: MenuProvider?
    get() {
        return if (title.hasInvisible()) {
            MenuManager.openedInventory[title.findInvisible()]
        } else {
            null
        }
    }

@Module(loadOrder = LoadOrder.API)
@PublishedApi
internal object MenuManager : AbstractModule() {
    internal var id: Int = 0
        get() {
            field++
            return field
        }
        private set

    @PublishedApi
    internal val openedInventory = mutableMapOf<Int, MenuProvider>()

    @EventHandler
    fun onSlotClick(event: InventoryClickEvent) {
        val topInventory = event.whoClicked.openInventory.topInventory ?: return
        if (topInventory.title == null || !topInventory.title.hasInvisible()) {
            return
        }
        val inventoryId = topInventory.title.findInvisible()
        val provider = openedInventory[inventoryId] ?: return
        provider.onInteract(event)
        if (event.clickedInventory === topInventory) {
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
        val targetInventory = event.whoClicked.openInventory.topInventory
        if (targetInventory.title.hasInvisible()) {
            val inventoryId = targetInventory.title.findInvisible()
            val provider = openedInventory[inventoryId] ?: return
            provider.onInteract(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (!event.inventory.title.hasInvisible()) {
            return
        }
        val inventoryId = event.inventory.title.findInvisible()
        val provider = openedInventory[inventoryId] ?: return
        if (!provider.onClose()) {
            provider.openInventory()
        } else {
            openedInventory.remove(inventoryId)
            HandlerList.unregisterAll(provider)
        }
    }

    @EventHandler
    fun onTick(a: TickEvent) {
        for (menu in openedInventory.values) {
            menu.tickCount++
            menu.onTick()
        }
    }
}

inline fun <reified T : MenuProvider> Player.getOpenedInventory(): T? {
    val title = player.openInventory.topInventory?.title
    if (title.isNullOrBlank() || !title.hasInvisible()) return null
    return MenuManager.openedInventory[title.findInvisible()] as? T
}