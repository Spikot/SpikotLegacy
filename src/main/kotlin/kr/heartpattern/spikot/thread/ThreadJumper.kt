@file:Suppress("unused")

package kr.heartpattern.spikot.thread

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * Run lambda in sync thread
 * @receiver Plugin to run this task
 * @param block lambda to run in sync thread
 */
fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) { // Fast routine
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

/**
 * Run lambda at next tick in sync thread
 * @receiver Plugin to run this task
 * @param block lambda to run at next tick in sync thread
 */
fun Plugin.runNextSync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskLater(this, block, 1)
}

/**
 * Run lambda in async thread
 * @receiver Plugin to run this task
 * @param block lambda to run in async thread
 */
fun Plugin.runAsync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(this, block)
}