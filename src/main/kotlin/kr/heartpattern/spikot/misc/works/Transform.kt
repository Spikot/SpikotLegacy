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

package kr.heartpattern.spikot.misc.works

/**
 * Represent transformed value
 */
interface Transform<T, U> {
    companion object {
        operator fun <T, U> invoke(before: T, after: U): Transform<T, U> {
            return SimpleTransform(before, after)
        }
    }

    val before: T
    val after: U

    data class SimpleTransform<T, U>(override val before: T, override val after: U) : Transform<T, U>
}

/**
 * Represent transformed value which after value is mutable
 */
interface MutableTransform<T, U> : Transform<T, U> {
    companion object {
        operator fun <T, U> invoke(before: T, after: U): MutableTransform<T, U> {
            return SimpleMutableTransform(before, after)
        }
    }

    override var after: U

    data class SimpleMutableTransform<T, U>(override val before: T, override var after: U) : MutableTransform<T, U>
}