package io.github.ReadyMadeProgrammer.Spikot.utils

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

fun <T : Any> KClass<T>.getInstance(): T? {
    return this.objectInstance
            ?: constructors.singleOrNull { it.parameters.all(KParameter::isOptional) }?.callBy(emptyMap())
}