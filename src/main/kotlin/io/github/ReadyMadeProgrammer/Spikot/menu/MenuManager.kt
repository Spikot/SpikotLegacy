@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.menu

import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.thread.sync
import io.github.ReadyMadeProgrammer.Spikot.utils.findInvisible
import io.github.ReadyMadeProgrammer.Spikot.utils.hasInvisible
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*
import org.bukkit.event.inventory.ClickType.*
import org.bukkit.event.inventory.InventoryAction.*
import org.bukkit.inventory.Inventory

fun Player.safeOpenInventory(inventory: Inventory) {
    sync {
        this.openInventory(inventory)
    }
}

fun Player.safeCloseInventory() {
    sync {
        this.closeInventory()
    }
}

fun Player.openInventory(menuProvider: MenuProvider) {
    menuProvider.initialize(MenuManager.id, this, MenuBuilder())
    MenuManager.openedInventory[menuProvider.id] = menuProvider
    menuProvider.onOpen()
    menuProvider.openInventory()
}

@Module(loadOrder = LoadOrder.API)
@Feature(SYSTEM_FEATURE)
object MenuManager : AbstractModule() {
    internal var id: Int = 0
        get() {
            field++
            return field
        }
        private set

    internal val openedInventory = mutableMapOf<Int, MenuProvider>()

    @EventHandler
    fun onSlotClick(event: InventoryClickEvent) {
        val topInventory = event.whoClicked.openInventory.topInventory ?: return
        if (topInventory.title == null || !topInventory.title.hasInvisible()) {
            return
        }
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (event.action) {
            NOTHING,
            DROP_ALL_CURSOR,
            DROP_ONE_CURSOR,
            DROP_ALL_SLOT,
            DROP_ONE_SLOT,
            HOTBAR_MOVE_AND_READD,
            HOTBAR_SWAP,
            CLONE_STACK,
            COLLECT_TO_CURSOR,
            InventoryAction.UNKNOWN -> return
        }
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (event.click) {
            WINDOW_BORDER_LEFT,
            WINDOW_BORDER_RIGHT,
            NUMBER_KEY,
            CONTROL_DROP,
            CREATIVE,
            ClickType.UNKNOWN -> return
        }
        val inventoryId = topInventory.title.findInvisible()
        val provider = openedInventory[inventoryId] ?: return
        provider.onInteract(event)
        if (event.clickedInventory === topInventory) {
            val slotId = event.slot
            val x = slotId % 9
            val y = slotId / 9
            val point = Point(x, y)
            val slot = provider.menu.slot[point] ?: return
            slot.clickHandler.forEach {
                it(point, event.click)
            }
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        if (event.inventory.title.hasInvisible()) {
            val inventoryId = event.inventory.title.findInvisible()
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
        }
    }
}