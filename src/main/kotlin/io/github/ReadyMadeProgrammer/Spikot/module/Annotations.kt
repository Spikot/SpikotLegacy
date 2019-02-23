package io.github.ReadyMadeProgrammer.Spikot.module

object LoadOrder {
    const val API = -100_000
    const val FASTEST = -20_000
    const val FAST = -10_000
    const val NORMAL = 0
    const val LATE = 10_000
    const val LATEST = 20_000
}

const val SYSTEM_FEATURE = "SYSTEM"

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Module(val loadOrder: Int = LoadOrder.NORMAL)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val value: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Disable