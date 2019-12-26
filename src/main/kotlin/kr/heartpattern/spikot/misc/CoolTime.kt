@file:Suppress("unused")

package kr.heartpattern.spikot.misc

import kotlin.math.max

/**
 * Cool time calculator
 * @param period CoolTime between each execution
 * @param lastUse Last used time
 */
@Suppress("MemberVisibilityCanBePrivate")
class CoolTime(val period: Long, var lastUse: Long = 0) {
    /**
     * Remain time to reuse
     */
    val remain: Long
        get() = max(lastUse + period - System.currentTimeMillis(), 0)

    /**
     * Whether cool time is past
     */
    val canUse: Boolean
        get() = remain == 0L

    /**
     * Mark last use to current time
     */
    fun use() {
        lastUse = System.currentTimeMillis()
    }

    /**
     * Run lambda if cool time is past
     * @param run lambda to execute if cool time is past
     * @return FailureHandler to handle if cool time is not past
     */
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

/**
 * Handle failure
 * @param isFailed Whether task is failed
 */
class FailureHandler(private val isFailed: Boolean) {
    /**
     * Run lambda if fail
     * @param handler lambda to execute if fail
     */
    fun onFail(handler: () -> Unit) {
        if (isFailed) {
            handler()
        }
    }
}