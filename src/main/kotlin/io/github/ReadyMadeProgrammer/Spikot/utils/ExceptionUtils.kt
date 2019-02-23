package io.github.ReadyMadeProgrammer.Spikot.utils

import kotlin.reflect.KClass

inline fun catchAll(run: () -> Unit) {
    try {
        run()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

inline fun <reified T : Throwable> catchOnly(run: () -> Unit) {
    try {
        run()
    } catch (e: Throwable) {
        if (e is T) {
            e.printStackTrace()
        } else {
            throw e
        }
    }
}

inline fun catchOnly(vararg types: KClass<out Exception>, run: () -> Unit) {
    try {
        run()
    } catch (e: Exception) {
        if (types.any { it.isInstance(e) }) {
            e.printStackTrace()
        } else {
            throw e
        }
    }
}