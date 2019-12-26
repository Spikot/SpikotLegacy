package kr.heartpattern.spikot.utils

import org.slf4j.Logger

/**
 * Catch all error and log it to logger with [message]
 * @receiver Logger to write error log
 * @param message Message to write when error occur
 * @param run lambda that can be throw error
 * @return Result of lambda if no error, otherwise null
 */
inline fun <R> Logger.catchAll(message: String, run: () -> R): R? {
    return try {
        run()
    } catch (e: Throwable) {
        warn(message, e)
        null
    }
}

/**
 * Catch all error and ignore it
 * @param run lambda that can be throw error
 * @return Result of lambda if no error, otherwise null
 */
inline fun <R> catchSilence(run: () -> R): R? {
    return try {
        run()
    } catch (ignored: Throwable) {
        null
    }
}