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

package kr.heartpattern.spikot.misc

/**
 * Simple optional type
 * @param T containing type
 */
sealed class Option<out T>

/**
 * Optional type with value
 * @param value Value of this optional value
 */
class Just<T>(val value: T) : Option<T>()

/**
 * Optional type without value
 */
object None : Option<Nothing>()

/**
 * Extract value from option or null
 * @receiver Option to extract
 * @return Value of option or null
 */
fun <T> Option<T>.getOrNull(): T? = (this as? Just<T>)?.value

/**
 * Extract value from option or default value
 * @receiver Option to extract
 * @param default Default value supplier
 * @receiver Value of option or default value
 */
inline fun <T> Option<T>.getOrElse(default: () -> T): T {
    return if (this is Just) {
        value
    } else {
        default()
    }
}

/**
 * Create Option from nullable value
 */
val <T : Any> T?.option: Option<T>
    get() = if (this == null) None else Just(this)

/**
 * Create Just from value
 */
val <T> T.just: Option<T>
    get() = Just(this)
