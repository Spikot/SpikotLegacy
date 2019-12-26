package kr.heartpattern.spikot.utils

/**
 * Builder which build list of type [T]
 * @param T Enclosing type of list
 */
class ListBuilder<T> {
    internal val data: MutableList<T> = ArrayList()

    /**
     * Add data to list
     * @receiver Data to add
     */
    operator fun T.unaryPlus() {
        data.add(this)
    }
}