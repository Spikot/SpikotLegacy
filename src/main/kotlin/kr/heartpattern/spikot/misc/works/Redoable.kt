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