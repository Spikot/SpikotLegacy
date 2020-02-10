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
 * Represent undo-able single work
 */
interface Undoable {
    /**
     * Whether this task is currently undo-able
     */
    val isUndoable: Boolean

    /**
     * Perform undo
     */
    fun undo()
}

/**
 * Partially implemented undo-able single work
 */
abstract class AbstractUndoable : Undoable {
    internal var isUndoed = false
    final override val isUndoable: Boolean
        get() = !isUndoed

    final override fun undo() {
        if (isUndoable) {
            isUndoed = true
            runUndo()
        } else {
            throw IllegalStateException("Already undoed")
        }
    }

    abstract fun runUndo()
}