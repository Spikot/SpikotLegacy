package io.github.ReadyMadeProgrammer.Spikot.coroutine

import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

@UseExperimental(InternalCoroutinesApi::class)
@PublishedApi
internal class BukkitDispatcher(@PublishedApi internal val plugin: Plugin, private val isSync: Boolean) : CoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive)
            return
        schedule(0, block::run)
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !isSync || !Bukkit.isPrimaryThread()
    }

    @UseExperimental(InternalCoroutinesApi::class)
    override suspend fun delay(time: Long) {
        super.delay(time)
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        val task = schedule(timeMillis) { block.run() }
        return object : DisposableHandle {
            override fun dispose() {
                task.cancel()
            }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = schedule(timeMillis) { with(continuation) { resumeUndispatched(Unit) } }
        continuation.invokeOnCancellation { task.cancel() }
    }

    private fun schedule(timeMillis: Long, runnable: () -> Unit) =
            if (isSync)
                Bukkit.getScheduler().runTaskLater(plugin, runnable, timeMillis / 50)
            else
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, timeMillis / 50)
}

suspend inline fun delayTick(tick: Long) {
    delay(tick * 50)
}

private val pluginSpecifiedScope = ConcurrentHashMap<String, Pair<CoroutineScope, CoroutineScope>>()
private fun getScope(plugin: Plugin): Pair<CoroutineScope, CoroutineScope> {
    return pluginSpecifiedScope.getOrPut(plugin.name) {
        Pair(
                CoroutineScope(CoroutineName("${plugin.name}-sync") + BukkitDispatcher(plugin, true) + SupervisorJob()),
                CoroutineScope(CoroutineName("${plugin.name}-async") + BukkitDispatcher(plugin, false) + SupervisorJob())
        )
    }
}

val Plugin.syncScope: CoroutineScope
    get() {
        val pair = getScope(this)
        return pair.first
    }

val Plugin.asyncScope: CoroutineScope
    get() {
        val pair = getScope(this)
        return pair.second
    }

val Plugin.syncDispatcher: CoroutineDispatcher
    get() = syncScope.coroutineContext[ContinuationInterceptor.Key] as CoroutineDispatcher
val Plugin.asyncDispatcher: CoroutineDispatcher
    get() = asyncScope.coroutineContext[ContinuationInterceptor.Key] as CoroutineDispatcher

inline fun Plugin.launchSync(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    return syncScope.launch(context, start) { block() }
}

inline fun Plugin.launchAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    return asyncScope.launch(context, start) { block() }
}

suspend inline fun <T> switchSync(
        context: CoroutineContext = EmptyCoroutineContext,
        crossinline block: suspend CoroutineScope.() -> T
): T {
    val interceptor = coroutineContext[ContinuationInterceptor]
    if (interceptor as? BukkitDispatcher == null)
        throw IllegalStateException("switchSync can only called from BukkitDispatcher")
    return withContext(context + interceptor.plugin.syncDispatcher) { block() }
}

suspend inline fun <T> switchAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        crossinline block: suspend CoroutineScope.() -> T): T {
    val interceptor = coroutineContext[ContinuationInterceptor]
    if (interceptor as? BukkitDispatcher == null)
        throw IllegalStateException("switchAsync can only called from BukkitDispatcher")
    return withContext(context + interceptor.plugin.asyncDispatcher) { block() }
}

inline fun <T> Plugin.async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        crossinline block: suspend CoroutineScope.() -> T): Deferred<T> {
    return asyncScope.async(context, start) { block() }
}

suspend inline fun <T> bukkitAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        crossinline block: suspend CoroutineScope.() -> T): Deferred<T> {
    val plugin = (coroutineContext[ContinuationInterceptor] as? BukkitDispatcher)?.plugin ?: spikotPlugin
    return plugin.asyncScope.async(context, start) { block() }
}