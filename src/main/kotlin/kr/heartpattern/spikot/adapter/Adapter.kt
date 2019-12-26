package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

/**
 * Annotate adapter implementation
 * @param target Implemented adapter class
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@FindAnnotation(impl = [IAdapter::class])
annotation class Adapter(
    val target: KClass<*> = Nothing::class
)

/**
 * Adapter implementation
 */
interface IAdapter : IModule