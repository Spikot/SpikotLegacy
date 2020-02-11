/*
 * Copyright 2020 Spikot project authors
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

import java.util.*

/**
 * Stack of redo-able tasks
 * @param T Type of redo-able task
 */
interface RedoStack<T : Redoable> : UndoStack<T> {
    /**
     * Whether there are any redo-able task
     */
    val isRedoable: Boolean

    /**
     * Perform redo
     */
    fun redo()
}

/**
 * Simple RedoStack implementation
 */
class SimpleRedoStack<T : Redoable> : RedoStack<T> {
    private val undoStack = Stack<T>()
    private val redoStack = Stack<T>()
    override val isUndoable: Boolean
        get() = undoStack.isNotEmpty()
    override val isRedoable: Boolean
        get() = redoStack.isNotEmpty()

    override fun push(work: T) {
        if (!work.isUndoable) {
            throw IllegalArgumentException("Not undoable value")
        } else {
            undoStack.push(work)
            redoStack.clear()
        }
    }

    override fun undo() {
        if (isUndoable) {
            val work = undoStack.pop()
            work.undo()
            redoStack.push(work)
        } else {
            throw IllegalArgumentException("No more undoable work")
        }
    }

    override fun redo() {
        if (isRedoable) {
            val work = redoStack.pop()
            work.redo()
            undoStack.push(work)
        } else {
            throw IllegalArgumentException("No more redoable work")
        }
    }
}