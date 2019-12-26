package kr.heartpattern.spikot.misc

/**
 * Simple optional type
 * @param T containing type
 */
sealed class Option<out T>

/**
 * Optional type with value
 * @param value Value of this optional value
 */
class Just<T>(val value: T) : Option<T>()

/**
 * Optional type without value
 */
object None : Option<Nothing>()