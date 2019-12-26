package kr.heartpattern.spikot.menu

import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.thread.runSync
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
        menu.slot.forEach { (point, item) ->
            player.openInventory.topInventory.setItem(point.x + point.y * 9, item.itemStack)
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
            spikot.runSync {
                updateInventory()
            }
        }
    }

    /**
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