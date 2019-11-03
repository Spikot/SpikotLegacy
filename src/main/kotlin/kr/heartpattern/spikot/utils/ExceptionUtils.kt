package kr.heartpattern.spikot.utils

import org.slf4j.Logger

inline fun <R> Logger.catchAll(message: String, run: () -> R): R? {
    return try {
        run()
    } catch (e: Throwable) {
        warn(message, e)
        null
    }
}

inline fun <R> catchSilence(run: () -> R): R? {
    return try {
        run()
    } catch (ignored: Throwable) {
        null
    }
}