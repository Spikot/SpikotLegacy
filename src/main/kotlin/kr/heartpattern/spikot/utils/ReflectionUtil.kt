package kr.heartpattern.spikot.utils

import java.lang.reflect.AccessibleObject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
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

inline fun <reified T : Annotation> KClass<*>.findAnnotations(): List<T> {
    return annotations.filterIsInstance<T>()
}

@UseExperimental(ExperimentalContracts::class)
inline fun <T : AccessibleObject> T.withAccessible(block: (T) -> Unit) {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val originalAccessibility = isAccessible
    isAccessible = true
    block(this)
    isAccessible = originalAccessibility
}