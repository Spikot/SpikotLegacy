package io.github.ReadyMadeProgrammer.Spikot.config

import org.bukkit.configuration.file.YamlConfiguration
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.full.declaredMemberProperties

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Config

abstract class ConfigSpec(
        internal val name: String? = null
) {
    internal lateinit var path: String
    internal lateinit var yaml: YamlConfiguration
    internal fun initialize() {
        this.javaClass.kotlin.declaredMemberProperties.stream().forEach { it.get(this) }
    }

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