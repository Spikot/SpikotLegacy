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