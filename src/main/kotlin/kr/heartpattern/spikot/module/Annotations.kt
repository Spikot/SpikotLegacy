package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.plugin.FindAnnotation

object LoadOrder {
    const val API = -100_000
    const val FASTEST = -20_000
    const val FAST = -10_000
    const val NORMAL = 0
    const val LATE = 10_000
    const val LATEST = 20_000
}


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [IModule::class])
annotation class Module(val loadOrder: Int = LoadOrder.NORMAL)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val value: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Disable