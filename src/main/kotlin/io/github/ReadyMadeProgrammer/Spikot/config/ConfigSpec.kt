package io.github.ReadyMadeProgrammer.Spikot.config

import org.bukkit.configuration.file.YamlConfiguration
import kotlin.properties.ReadWriteProperty

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Config

abstract class ConfigSpec(
        internal val name: String? = null
) {
    internal lateinit var path: String
    internal lateinit var yaml: YamlConfiguration
    protected fun <T : Any> require(name: String, default: T): ReadWriteProperty<ConfigSpec, T> {
        return ConfigProperty(default, name)
    }

    protected fun <T : Any> require(default: T): ReadWriteProperty<ConfigSpec, T> {
        return ConfigProperty(default)
    }

    protected inline fun <reified T : Any> requireList(name: String): ReadWriteProperty<ConfigSpec, MutableList<T>> {
        return ListConfigProperty(T::class, name)
    }

    protected inline fun <reified T : Any> requireList(): ReadWriteProperty<ConfigSpec, MutableList<T>> {
        return ListConfigProperty(T::class)
    }
}