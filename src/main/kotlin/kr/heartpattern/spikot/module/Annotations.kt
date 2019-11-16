package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [IModule::class])
annotation class Module(val depend: Array<KClass<*>> = [])

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadBefore(val value: Array<KClass<*>> = [])

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val value: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Disable