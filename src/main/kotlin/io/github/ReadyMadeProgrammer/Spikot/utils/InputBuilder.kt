package io.github.ReadyMadeProgrammer.Spikot.utils

class InputBuilder<T> {
    internal val data: MutableList<T> = ArrayList()
    operator fun T.unaryPlus() {
        data.add(this)
    }
}