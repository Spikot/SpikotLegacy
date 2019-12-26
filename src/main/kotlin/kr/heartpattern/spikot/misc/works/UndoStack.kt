@file:Suppress("unused")

package kr.heartpattern.spikot.misc.works

import java.util.*

/**
 * Stack of undo-able task
 * @param T Type of undo-able task
 */
interface UndoStack<T : Undoable> {
    /**
     * Whether there are any undo-able task
     */
    val isUndoable: Boolean

    /**
     * Add undo-able task
     * @param work Undo-able task to add
     */
    fun push(work: T)

    /**
     * Perform undo
     */
    fun undo()
}

/**
 * Simple UndoStack implementation
 */
class SimpleUndoStack<T : Undoable> : UndoStack<T> {
    private val stack = Stack<T>()
    override val isUndoable: Boolean
        get() = stack.isNotEmpty()

    override fun push(work: T) {
        if (!work.isUndoable) {
            throw IllegalArgumentException("Not undoable value")
        } else {
            stack.push(work)
        }
    }

    override fun undo() {
        if (isUndoable) {
            stack.pop().undo()
        } else {
            throw IllegalArgumentException("No more undoable work")
        }
    }
}