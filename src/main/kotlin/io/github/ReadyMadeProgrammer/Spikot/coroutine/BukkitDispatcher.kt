package io.github.ReadyMadeProgrammer.Spikot.coroutine

import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.CoroutineContext

@InternalCoroutinesApi
class BukkitDispatcher(private val plugin: Plugin, private val synchronizationType: SynchronizationType) : CoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) {
            return
        }
        if (Bukkit.isPrimaryThread() && synchronizationType == SynchronizationType.SYNC) {
            block.run()
        } else {
            if (synchronizationType == SynchronizationType.SYNC) {
                Bukkit.getScheduler().runTask(plugin, block)
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, block)
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = run<BukkitTask> {
            if (synchronizationType == SynchronizationType.SYNC) {
                Bukkit.getScheduler().runTaskLater(plugin, Runnable { continuation.apply { resumeUndispatched(Unit) } }, timeMillis / 50)
            } else {
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable { continuation.apply { resumeUndispatched(Unit) } }, timeMillis / 50)

            }
        }
        continuation.invokeOnCancellation { task.cancel() }
    }
}