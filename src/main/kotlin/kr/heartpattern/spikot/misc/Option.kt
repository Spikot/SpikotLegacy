package kr.heartpattern.spikot.misc

sealed class Option<out T>

class Just<T>(val value: T) : Option<T>()

object None : Option<Nothing>()