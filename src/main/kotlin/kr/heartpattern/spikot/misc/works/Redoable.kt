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

@file:Suppress("unused")

package kr.heartpattern.spikot.misc.works

/**
 * Represent redo-able single work
 */
interface Redoable : Undoable {
    /**
     * Whether this work is currently redo-able.
     */
    val isRedoable: Boolean

    /**
     * Redo this work
     * @throws IllegalStateException if work is already redo.
     */
    @Throws(IllegalStateException::class)
    fun redo()
}

/**
 * Partially implemented redo-able single work
 */
abstract class AbstractRedoable : AbstractUndoable(), Redoable {
    final override val isRedoable: Boolean
        get() = isUndoed

    final override fun redo() {
        if (isRedoable) {
            isUndoed = false
            runRedo()
        } else {
            throw IllegalStateException("Didn't undoed")
        }
    }

    abstract fun runRedo()
}