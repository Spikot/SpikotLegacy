package kr.heartpattern.spikot.misc.works

interface Undoable {
    val isUndoable: Boolean
    fun undo()
}

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