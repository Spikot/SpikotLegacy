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

/**
 * Get object instance of class or create new instance
 * @receiver KClass to get instance
 * @return Object of instance or create new instance if possible, otherwise null
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.getInstance(): T? {
    return if (this.visibility != KVisibility.PUBLIC) { //Do dirty stuff
        try {
            val instanceField = this.java.getDeclaredField("INSTANCE")
            instanceField.isAccessible = true
            instanceField.get(null) as T
        } catch (e: Throwable) {
            constructors
                .singleOrNull { it.parameters.all(KParameter::isOptional) }
                ?.apply { isAccessible = true }
                ?.callBy(emptyMap())
        }
    } else {
        this.objectInstance ?: createInstance()
    }
}

/**
 * Find all annotation with given type [T]
 * @receiver KClass to find annotation
 * @param T Annotation type
 * @return List of all annotation with type [T]
 */
inline fun <reified T : Annotation> KClass<*>.findAnnotations(): List<T> {
    return annotations.filterIsInstance<T>()
}

/**
 * Get access, run block, and restore to original value
 * @receiver AccessibleObject to get access
 * @param block lambda to run with access
 */
@UseExperimental(ExperimentalContracts::class)
inline fun <T : AccessibleObject> T.withAccessible(block: (T) -> Unit) {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val originalAccessibility = isAccessible
    isAccessible = true
    block(this)
    isAccessible = originalAccessibility
}