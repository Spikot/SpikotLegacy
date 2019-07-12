package io.github.ReadyMadeProgrammer.Spikot.persistence

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Data(val targetClass: KClass<*> = Nothing::class)