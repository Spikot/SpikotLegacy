package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

/**
 * Annotate root command to register.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation
annotation class Root

/**
 * Name of command
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Name(vararg val name: String)

/**
 * Usage of command
 * @param usage Usage message
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Usage(val usage: String)

/**
 * Help of command
 * @param help Help message
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Help(val help: String)

/**
 * List of child of command
 * @param child List of child
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class Child(val child: KClass<out Command>)