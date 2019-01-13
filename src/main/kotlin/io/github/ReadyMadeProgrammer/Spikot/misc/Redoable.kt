@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.misc

/**
 * Represent redoable work
 */
interface Redoable : Undoable {
    /**
     * Whether this work is redoable.
     */
    val isRedoable: Boolean

    /**
     * Redo this work
     * @throws IllegalStateException if work is already redo.
     */
    fun redo()
}

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