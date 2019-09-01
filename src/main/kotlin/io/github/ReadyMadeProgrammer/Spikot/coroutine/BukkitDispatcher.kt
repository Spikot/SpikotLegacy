package io.github.ReadyMadeProgrammer.Spikot.coroutine

import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.MainDispatcherFactory
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

@UseExperimental(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val Dispatchers.Bukkit: DispatcherBukkit
    get() = BukkitDispatcher

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
sealed class DispatcherBukkit : MainCoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Bukkit.getScheduler().runTask(spikotPlugin, block)
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = Bukkit.getScheduler().runTaskLater(spikotPlugin, {
            with(continuation) {
                resumeUndispatched(Unit)
            }
        }, timeMillis / 50)
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        val task = Bukkit.getScheduler().runTaskLater(spikotPlugin, block, timeMillis / 50)
        return object : DisposableHandle {
            override fun dispose() {
                task.cancel()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal class BukkitDispatcherFactory : MainDispatcherFactory {
    override val loadPriority: Int
        get() = 0

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher = BukkitDispatcher
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
private object ImmediateBukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = this

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = !Bukkit.isPrimaryThread()

    override fun toString() = "Bukkit [immediate]"
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal object BukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = ImmediateBukkitDispatcher

    override fun toString() = "Bukkit"
}

suspend fun delayTick(tick: Long) {
    delay(tick * 50)
}