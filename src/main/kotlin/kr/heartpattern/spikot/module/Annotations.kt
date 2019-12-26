package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

/**
 * Annotate module
 * @param depend Array of module which is required for this module
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [IModule::class])
annotation class Module(val depend: Array<KClass<*>> = [])

/**
 * Annotate module should load before given modules
 * @param value Array of module which should be load after this module
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadBefore(val value: Array<KClass<*>> = [])

/**
 * Annotate module should load only when feature is enabled
 * @param value Required feature
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val value: String)

/**
 * Annotate module should not be loaded
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Disable