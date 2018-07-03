package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import io.github.ReadyMadeProgrammer.Spikot.findInvisible
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.scheduler.BukkitRunnable

fun InventoryClickEvent.closeInventory() {
    object : BukkitRunnable() {
        override fun run() {
            this@closeInventory.whoClicked.closeInventory()
        }
    }.runTask(spikotPlugin)
}

fun InventoryClickEvent.openInventory(inventory: Inventory) {
    object : BukkitRunnable() {
        override fun run() {
            this@openInventory.whoClicked.openInventory(inventory)
        }
    }
}

object MenuEventReciever : Listener {
    internal val menus = hashMapOf<Int, Menu>()
    @EventHandler(priority = EventPriority.LOW)
    fun onClose(e: InventoryCloseEvent) {
        if (e.player !is Player) return
        try {
            val code = e.inventory.getCode()
            val m = menus[code] ?: return
            if (m.menu.isClosable) {
                val result = m.onClose(e.player as Player, e.inventory)
                if (!result) e.player.openInventory(m.menu.inventory)
            } else {
                e.player.openInventory(m.menu.inventory)
            }
        } catch (e: IllegalArgumentException) {
            //Do Nothing
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onClick(e: InventoryClickEvent) {
        if (e.whoClicked.openInventory.topInventory.size >= e.rawSlot || e.rawSlot < 0) return
        try {
            val code = e.inventory.getCode()
            val m = menus[code] ?: return
            val slot = m.menu[e.slot] ?: return // Find exact number
            slot.events.forEach { it(m, MenuEvent(e.whoClicked as Player, slot, e.click)) }
            e.isCancelled = true
        } catch (e: Exception) {
            //Do Nothing
        }
    }

    private fun Inventory.getCode(): Int {
        return this.title.findInvisible()
    }
}