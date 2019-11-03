package kr.heartpattern.spikot.menu

import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.thread.runSync
import kr.heartpattern.spikot.utils.nonnull
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.Inventory

abstract class MenuProvider : AbstractModule() {
    val id: Int by contextDelegate(MenuIdProperty).nonnull()
    val player: Player by contextDelegate(MenuPlayerProperty).nonnull()
    val menu: MenuBuilder by contextDelegate(MenuBuilderProperty).nonnull()
    val inventory: Inventory by contextDelegate(MenuInventoryProperty).nonnull()
    val isOpen: Boolean by contextDelegate(MenuOpenProperty)

    private fun updateInventory() {
        menu.slot.forEach { (point, item) ->
            player.openInventory.topInventory.setItem(point.x + point.y * 9, item.itemStack)
        }
        player.updateInventory()
    }

    @Suppress("unused")
    protected fun update(build: MenuBuilder.() -> Unit) {
        menu.build()
        if (isOpen) {
            spikot.runSync {
                updateInventory()
            }
        }
    }

    open fun onClose(): Boolean {
        return true
    }

    open fun onInteract(event: InventoryInteractEvent) {
        event.isCancelled = true
    }

    open fun onDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }
}