@file:Suppress("unused")
package io.github.ReadyMadeProgrammer.Spikot.utils

import co.aikar.taskchain.BukkitTaskChainFactory
import co.aikar.taskchain.TaskChain
import co.aikar.taskchain.TaskChainFactory
import org.bukkit.plugin.Plugin

internal var taskChainFactory: TaskChainFactory? = null

internal fun initTaskChain(plugin: Plugin){
    taskChainFactory = BukkitTaskChainFactory.create(plugin)
}

fun <T> chain() = taskChainFactory!!.newChain<T>()!!
fun <T> sharedChain(name: String) = taskChainFactory!!.newSharedChain<T>(name)!!

fun <T> sync(body: () -> T) = chain<T>().syncFirst(body)!!
fun <T> async(body: () -> T) = chain<T>().asyncFirst(body)!!
fun <T> current(body: () -> T) = chain<T>().currentFirst(body)!!
operator fun TaskChain<*>.invoke() = this.execute()