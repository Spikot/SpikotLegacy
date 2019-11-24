package kr.heartpattern.spikot.command

import kotlin.reflect.KProperty

internal class ValidationException(val pos: Int) : Exception()

class PropertyNotInitializedException(property: KProperty<*>): Exception()