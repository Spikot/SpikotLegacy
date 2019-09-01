@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.thread

import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) { // Fast routine
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

fun Plugin.runNextSync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskLater(this, block, 1)
}

fun Plugin.runAsync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(this, block)
}

typealias ThreadWork<T, R> = (T) -> R

@Deprecated("Use coroutine")
fun abort(): Nothing {
    throw AbortException()
}

@Deprecated("Use coroutine")
class AbortException : Exception()

@Deprecated(
    message = "Use coroutine",
    replaceWith = ReplaceWith("Plugin.async{ }", "io.github.ReadyMadeProgrammer.Spiko.coroutine.*"),
    level = DeprecationLevel.WARNING
)
fun <R : Any> async(runnable: ThreadWork<Unit, R>): ThreadChain<Unit, R> {
    val chain = ThreadChain(type = ThreadType.ASYNC, runnable = runnable)
    chain.execute(Unit)
    return chain
}

@Deprecated(
    message = "Use coroutine",
    replaceWith = ReplaceWith("Plugin.sync{ }", "io.github.ReadyMadeProgrammer.Spiko.coroutine.*"),
    level = DeprecationLevel.WARNING
)
fun <R : Any> sync(runnable: ThreadWork<Unit, R>): ThreadChain<Unit, R> {
    val chain = ThreadChain(type = ThreadType.SYNC, runnable = runnable)
    chain.execute(Unit)
    return chain
}

@Deprecated(
    message = "Use coroutine",
    replaceWith = ReplaceWith("Plugin.sync{ delay(millis) }", "io.github.ReadyMadeProgrammer.Spiko.coroutine.*"),
    level = DeprecationLevel.WARNING
)
fun delay(tick: Int): ThreadChain<Unit, Unit> {
    {}
    return Plugin.sync { }.delay(tick)
}

@Deprecated("Use coroutine")
@Suppress("MemberVisibilityCanBePrivate")
data class ThreadChain<T : Any, R : Any>(val type: ThreadType, val runnable: ThreadWork<T, R>) {
    private var value: R? = null
    private var runned = false
    private var next: ThreadChain<R, *>? = null
    fun <N : Any> sync(runnable: ThreadWork<R, N>): ThreadChain<R, N> {
        next = ThreadChain(type = ThreadType.SYNC, runnable = runnable)
        if (runned) {
            next!!.execute(value!!)
        }
        @Suppress("UNCHECKED_CAST")
        return next as ThreadChain<R, N>
    }

    fun <N : Any> async(runnable: ThreadWork<R, N>): ThreadChain<R, N> {
        next = ThreadChain(type = ThreadType.ASYNC, runnable = runnable)
        if (runned) {
            next!!.execute(value!!)
        }
        @Suppress("UNCHECKED_CAST")
        return next as ThreadChain<R, N>
    }

    fun delay(tick: Int): ThreadChain<R, R> {
        return async { v ->
            Thread.sleep(tick * 50L)
            v
        }
    }

    internal fun execute(v: T) {
        if (type == ThreadType.SYNC) {
            object : BukkitRunnable() {
                override fun run() {
                    try {
                        value = runnable(v)
                        runned = true
                        if (next != null) {
                            next!!.execute(value!!)
                        }
                    } catch (e: AbortException) {

                    }
                }
            }.runTask(spikotPlugin)
        } else {
            object : BukkitRunnable() {
                override fun run() {
                    try {
                        value = runnable(v)
                        runned = true
                        if (next != null) {
                            next!!.execute(value!!)
                        }
                    } catch (e: AbortException) {

                    }
                }
            }.runTaskAsynchronously(spikotPlugin)
        }
    }
}

@Deprecated("Use coroutine")
enum class ThreadType {
    SYNC, ASYNC
}