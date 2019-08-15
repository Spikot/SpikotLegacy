package io.github.ReadyMadeProgrammer.Spikot.utils

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible

fun <T : Any> KClass<T>.getInstance(): T? {
    if (this.visibility != KVisibility.PUBLIC) { //Do dirty stuff
        try {
            val instanceField = this.java.getDeclaredField("INSTANCE")
            instanceField.isAccessible = true
            return instanceField.get(null) as T
        } catch (e: Throwable) {
            constructors
                    .singleOrNull { it.parameters.all(KParameter::isOptional) }
                    ?.apply { isAccessible = true }
                    ?.callBy(emptyMap())
        }

    }
    return this.objectInstance ?: createInstance()
}