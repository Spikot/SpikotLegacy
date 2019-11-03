@file:Suppress("unused")

package kr.heartpattern.spikot.thread

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) { // Fast routine
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

fun Plugin.runNextSync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskLater(this, block, 1)
}

fun Plugin.runAsync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(this, block)
}