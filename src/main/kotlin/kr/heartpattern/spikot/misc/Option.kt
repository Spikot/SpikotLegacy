package kr.heartpattern.spikot.misc

sealed class Option<out T>

class Just<T>(val value: T) : Option<T>()

object None : Option<Nothing>()

fun <T> Option<T>.getOrNull(): T? = (this as? Just<T>)?.value

fun <T> Option<T>.getOrElse(default: ()->T): T = (this as? Just<T>)?.value?:default()

val <T: Any> T?.option: Option<T>
    get() = if(this == null) None else Just(this)

val <T> T.just: Option<T>
    get() = Just(this)