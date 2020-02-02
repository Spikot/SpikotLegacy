/*
 * Copyright 2020 HeartPattern
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.utils

import org.slf4j.Logger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Catch all error and log it to logger with [message]
 * @receiver Logger to write error log
 * @param message Message to write when error occur
 * @param run lambda that can be throw error
 * @return Result of lambda if no error, otherwise null
 */
@UseExperimental(ExperimentalContracts::class)
inline fun <R> Logger.catchAll(message: String, run: () -> R): R? {
    contract { callsInPlace(run, InvocationKind.EXACTLY_ONCE) }
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
@UseExperimental(ExperimentalContracts::class)
inline fun <R> catchSilence(run: () -> R): R? {
    contract { callsInPlace(run, InvocationKind.EXACTLY_ONCE) }
    return try {
        run()
    } catch (ignored: Throwable) {
        null
    }
}