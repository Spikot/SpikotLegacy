package kr.heartpattern.spikot.command

import kotlin.reflect.KProperty

internal class ValidationException(val pos: Int) : Exception()

/**
 * Thrown when access is made for uninitialized CommandProperty
 * @param property Property which is made this exception
 */
class PropertyNotInitializedException(property: KProperty<*>): Exception()