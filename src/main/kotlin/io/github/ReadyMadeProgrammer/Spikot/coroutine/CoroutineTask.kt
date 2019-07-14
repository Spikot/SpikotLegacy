package io.github.ReadyMadeProgrammer.Spikot.coroutine

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.resume

class CoroutineTask internal constructor(private val controller: BukkitSchedulerController) {
    val plugin: Plugin
        get() = controller.plugin
    val currentTask: BukkitTask?
        get() = controller.currentTask
    val synchronizationType: SynchronizationType
        get() = if (controller.currentTask?.isSync == true) SynchronizationType.SYNC else SynchronizationType.ASYNC

    fun cancel() {
        controller.resume(Unit)
    }
}

internal class RepetitionContinuation(val resume: (Long) -> Unit, val delay: Long = 0) {
    var passedTicks = 0L
    private var resumed = false

    fun tryResume(passedTicks: Long) {
        if (resumed) {
            throw IllegalStateException("Already resumed")
        }

        this.passedTicks += passedTicks
        if (this.passedTicks >= delay) {
            resumed = true
            resume(this.passedTicks)
        }
    }
}