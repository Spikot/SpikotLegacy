package io.github.ReadyMadeProgrammer.Spikot.coroutine

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

internal interface TaskScheduler {
    val currentTask: BukkitTask?
    fun doWait(ticks: Long, task: (Long) -> Unit)
    fun doYield(task: (Long) -> Unit)
    fun doContextSwitch(context: SynchronizationType, task: (Boolean) -> Unit)
    fun forceNewContext(context: SynchronizationType, task: () -> Unit)
}

internal class NonRepeatingTaskScheduler(val plugin: Plugin, val scheduler: BukkitScheduler) : TaskScheduler {
    override var currentTask: BukkitTask? = null
    override fun doWait(ticks: Long, task: (Long) -> Unit) {
        currentTask = if (currentContext() == SynchronizationType.SYNC) scheduler.runTaskLater(plugin, { task(ticks) }, ticks)
        else scheduler.runTaskLaterAsynchronously(plugin, { task(ticks) }, ticks)
    }

    override fun doYield(task: (Long) -> Unit) {
        doWait(0, task)
    }

    override fun doContextSwitch(context: SynchronizationType, task: (Boolean) -> Unit) {
        val currentContext = currentContext()
        if (currentContext == context) {
            task(false)
        } else {
            forceNewContext(context) { task(true) }
        }
    }

    override fun forceNewContext(context: SynchronizationType, task: () -> Unit) {
        if (context == SynchronizationType.SYNC) scheduler.runTask(plugin, task)
        else scheduler.runTaskAsynchronously(plugin, task)
    }
}

internal class RepeatingTaskScheduler(val plugin: Plugin, val scheduler: BukkitScheduler, val interval: Long) : TaskScheduler {
    override var currentTask: BukkitTask? = null
    private var nextContinuation: RepetitionContinuation? = null

    override fun doWait(ticks: Long, task: (Long) -> Unit) {
        nextContinuation = RepetitionContinuation(task, ticks)
    }

    override fun doYield(task: (Long) -> Unit) {
        nextContinuation = RepetitionContinuation(task)
    }

    override fun doContextSwitch(context: SynchronizationType, task: (Boolean) -> Unit) {
        val currentContext = currentContext()
        if (context == currentContext) task(false)
        else forceNewContext(context) { task(true) }
    }

    override fun forceNewContext(context: SynchronizationType, task: () -> Unit) {
        doYield { task() }
        currentTask?.cancel()
        val nextTask: () -> Unit = { nextContinuation?.tryResume(interval) }
        currentTask = if (context == SynchronizationType.SYNC) scheduler.runTaskTimer(plugin, nextTask, 0L, interval)
        else scheduler.runTaskTimerAsynchronously(plugin, nextTask, 0L, interval)
    }
}