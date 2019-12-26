package kr.heartpattern.spikot.plugin

import kotlin.reflect.KClass

/**
 * Annotate annotation to be record in compile time.
 * Annotation which annotated with this can be found by SpikotPluginManager.forEachAnnotation
 * @param impl Array of class which should be implemented by class that annotated with this annotation.
 * Validation of it is done in compile time and occur compile error.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class FindAnnotation(
    val impl: Array<KClass<*>> = []
)