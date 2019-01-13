@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.misc

import kotlin.math.max

/**
 * Cool time calculator
 */
@Suppress("MemberVisibilityCanBePrivate")
class CoolTime(val period: Long, var lastUse: Long = 0) {
    val remain: Long
        get() = max(lastUse + period - System.currentTimeMillis(), 0)
    val canUse: Boolean
        get() = remain == 0L

    fun use() {
        lastUse = System.currentTimeMillis()
    }

    fun use(run: () -> Unit): FailureHandler {
        return FailureHandler(if (canUse) {
            run()
            use()
            false
        } else {
            true
        })
    }
}

class FailureHandler(private val isFailed: Boolean) {
    fun onFail(handler: () -> Unit) {
        if (isFailed) {
            handler()
        }
    }
}