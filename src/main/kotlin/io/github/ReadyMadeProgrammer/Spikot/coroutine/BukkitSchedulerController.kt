package io.github.ReadyMadeProgrammer.Spikot.coroutine

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.*

@RestrictsSuspension
class BukkitSchedulerController(
        val plugin: Plugin,
        val scheduler: BukkitScheduler
) : Continuation<Unit> {
    private var schedulerDelegate: TaskScheduler = NonRepeatingTaskScheduler(plugin, scheduler)
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    val currentTask: BukkitTask?
        get() = schedulerDelegate.currentTask
    val isRepeating: Boolean
        get() = schedulerDelegate is RepeatingTaskScheduler

    internal suspend fun start(initialContext: SynchronizationType) = suspendCoroutine<Unit> { continuation ->
        schedulerDelegate.doContextSwitch(initialContext) { continuation.resume(Unit) }
    }

    internal fun cleanup() {
        currentTask?.cancel()
    }

    override fun resumeWith(result: Result<Unit>) {
        cleanup()
        result.getOrThrow()
    }

    suspend fun wait(ticks: Long): Long = suspendCoroutine { continuation ->
        schedulerDelegate.doWait(ticks, continuation::resume)
    }

    suspend fun yield(): Long = suspendCoroutine { continuation ->
        schedulerDelegate.doYield(continuation::resume)
    }

    suspend fun switchContext(context: SynchronizationType): Boolean = suspendCoroutine { continuation ->
        schedulerDelegate.doContextSwitch(context, continuation::resume)
    }

    suspend fun forceSwitchContext(context: SynchronizationType) = suspendCoroutine<Unit> { continuation ->
        schedulerDelegate.forceNewContext(context) { continuation.resume(Unit) }
    }

    suspend fun repeating(interval: Long): Long = suspendCoroutine { continuation ->
        schedulerDelegate = RepeatingTaskScheduler(plugin, scheduler, interval)
        schedulerDelegate.forceNewContext(currentContext()) {
            continuation.resume(0)
        }
    }
}