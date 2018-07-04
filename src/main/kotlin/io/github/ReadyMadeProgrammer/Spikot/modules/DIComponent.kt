package io.github.ReadyMadeProgrammer.Spikot.modules

import io.github.ReadyMadeProgrammer.Spikot.ServerVersion
import io.github.ReadyMadeProgrammer.Spikot.ServerVersion.Platform.*
import org.bukkit.event.Listener
import org.koin.dsl.module.Module
import org.koin.standalone.KoinComponent

/**
 * Shorter name of KoinComponent
 */
typealias Component = KoinComponent

/**
 * Annotate Super class of bunch of Service
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Contract

/**
 * Annotate Service which will inject into.
 * Class annotated with Service must implement Component
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Service(val name: String = "")

/**
 * Annotate Service which will only use when server version is compact.
 * Class annotated with Adapter must annotated with Service too.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Adapter(val platform: Array<ServerVersion.Platform> = [UNKNOWN, SPIGOT, PAPER, GLOWSTONE, CRAFT], val version: Array<String> = ["0.0.0+"])

/**
 * Annotate Service which should be singleton
 * Class annotated with Singleton must annotated with Service too.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Singleton

/**
 * Annotate Service which is only use when feature is defined in feature.txt
 * Class annotated with Feature must annotated with Service too.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val feature: String, val enable: Boolean = true)

/**
 * Class implements Module will load when server start and that class is entry point of spikot plugin
 */
interface Module : KoinComponent, Listener {
    fun onStart()
    fun onStop()
}

/**
 * Class implements ModuleConfig will load before dependency injection start, and use for dependency injecting.
 */
interface ModuleConfig {
    val module: Module
}