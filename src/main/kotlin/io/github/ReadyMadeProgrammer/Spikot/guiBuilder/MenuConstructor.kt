package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

fun Player.open(menu: Menu) {
    object : BukkitRunnable() {
        override fun run() {
            this@open.openInventory(menu.menu.inventory)
        }
    }.runTask(spikotPlugin)
}