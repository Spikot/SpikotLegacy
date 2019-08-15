package io.github.ReadyMadeProgrammer.Spikot.plugin

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class FindAnnotation(
        val impl: Array<KClass<*>> = []
)