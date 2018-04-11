package io.github.ReadyMadeProgrammer.Spikot

import co.aikar.taskchain.BukkitTaskChainFactory
import co.aikar.taskchain.TaskChainFactory
import org.bukkit.plugin.Plugin

internal var taskChainFactory: TaskChainFactory? = null

internal fun initTaskChain(plugin: Plugin){
    taskChainFactory = BukkitTaskChainFactory.create(plugin)
}

fun <T> chain() = taskChainFactory!!.newChain<T>()
fun <T> sharedChain(name: String) = taskChainFactory!!.newSharedChain<T>(name)

