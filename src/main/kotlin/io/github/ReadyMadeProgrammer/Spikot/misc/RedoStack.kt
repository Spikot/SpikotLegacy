package io.github.ReadyMadeProgrammer.Spikot.misc

import java.util.*

interface RedoStack<T : Redoable> : UndoStack<T> {
    val isRedoable: Boolean
    fun redo()
}

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