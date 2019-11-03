package kr.heartpattern.spikot.utils

class InputBuilder<T> {
    internal val data: MutableList<T> = ArrayList()
    operator fun T.unaryPlus() {
        data.add(this)
    }
}