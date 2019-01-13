package io.github.ReadyMadeProgrammer.Spikot.thread

import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import org.bukkit.scheduler.BukkitRunnable

typealias ThreadWork<T, R> = (T) -> R

fun abort(): Nothing {
    throw AbortException()
}

class AbortException : Exception()

fun <R : Any> async(runnable: ThreadWork<Unit, R>): ThreadChain<Unit, R> {
    val chain = ThreadChain(type = ThreadType.ASYNC, runnable = runnable)
    chain.execute(Unit)
    return chain
}

fun <R : Any> sync(runnable: ThreadWork<Unit, R>): ThreadChain<Unit, R> {
    val chain = ThreadChain(type = ThreadType.SYNC, runnable = runnable)
    chain.execute(Unit)
    return chain
}

fun delay(tick: Int): ThreadChain<Unit, Unit> {
    return sync {}.delay(tick)
}

data class ThreadChain<T : Any, R : Any>(val type: ThreadType, val runnable: ThreadWork<T, R>) {
    private var value: R? = null
    private var runned = false
    private var next: ThreadChain<R, *>? = null
    fun <N : Any> sync(runnable: ThreadWork<R, N>): ThreadChain<R, N> {
        next = ThreadChain<R, N>(type = ThreadType.SYNC, runnable = runnable)
        if (runned) {
            next!!.execute(value!!)
        }
        return next as ThreadChain<R, N>
    }

    fun <N : Any> async(runnable: ThreadWork<R, N>): ThreadChain<R, N> {
        next = ThreadChain<R, N>(type = ThreadType.ASYNC, runnable = runnable)
        if (runned) {
            next!!.execute(value!!)
        }
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

enum class ThreadType {
    SYNC, ASYNC
}