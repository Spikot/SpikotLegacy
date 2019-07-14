package io.github.ReadyMadeProgrammer.Spikot.coroutine

import kotlinx.coroutines.InternalCoroutinesApi
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume


@InternalCoroutinesApi
val Plugin.sync: BukkitDispatcher
    get() = BukkitDispatcher(this, SynchronizationType.SYNC)
@InternalCoroutinesApi
val Plugin.async: BukkitDispatcher
    get() = BukkitDispatcher(this, SynchronizationType.ASYNC)

fun currentContext() = if (Bukkit.isPrimaryThread()) SynchronizationType.SYNC else SynchronizationType.ASYNC

fun Plugin.runWith(initialContext: SynchronizationType, coroutine: suspend BukkitSchedulerController.() -> Unit): CoroutineTask {
    val controller = BukkitSchedulerController(this, Bukkit.getScheduler())
    val block: suspend BukkitSchedulerController.() -> Unit = {
        try {
            start(initialContext)
            coroutine()
        } finally {
            cleanup()
        }
    }
    block.createCoroutine(receiver = controller, completion = controller).resume(Unit)
    return CoroutineTask(controller)
}

fun Plugin.runSync(coroutine: suspend BukkitSchedulerController.() -> Unit): CoroutineTask = runWith(SynchronizationType.SYNC, coroutine)

fun Plugin.runAsync(coroutine: suspend BukkitSchedulerController.() -> Unit): CoroutineTask = runWith(SynchronizationType.ASYNC, coroutine)

suspend inline fun BukkitSchedulerController.switchSync(): Boolean = switchContext(SynchronizationType.SYNC)

suspend inline fun BukkitSchedulerController.switchAsync(): Boolean = switchContext(SynchronizationType.ASYNC)
