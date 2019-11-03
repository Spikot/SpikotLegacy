@file:Suppress("unused")

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.misc.*
import kr.heartpattern.spikot.utils.nonnull
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.event.Listener
import java.io.File
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface IModule : Listener {
    object StateProperty : Property<State>
    object PluginProperty : Property<SpikotPlugin>

    enum class State(val readable: String) {
        CREATE("create"),
        LOAD("load"),
        ENABLE("enable"),
        DISABLE("disable"),
        ERROR("error")
    }

    val context: MutablePropertyMap

    fun onLoad(context: MutablePropertyMap) {}

    fun onEnable() {}

    fun onDisable() {}
}

abstract class AbstractModule : IModule {
    final override lateinit var context: MutablePropertyMap

    protected fun <T> contextDelegate(prop: Property<T>): ReadOnlyProperty<Any, T?> = object : ReadOnlyProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return context[prop]
        }
    }

    protected fun contextDelegate(prop: FlagProperty): ReadOnlyProperty<Any, Boolean> = object : ReadOnlyProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return context[prop]
        }
    }

    protected fun <T> mutableContextDelegate(prop: MutableProperty<T>): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return context[prop]
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            context[prop] = value
        }
    }

    protected fun mutableContextDelegate(prop: MutableFlagProperty): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return context[prop]
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            context[prop] = value
        }
    }

    protected val plugin: SpikotPlugin by contextDelegate(IModule.PluginProperty).nonnull()
    protected val logger: KLogger by lazy { KotlinLogging.logger("${plugin.name}-${this::class.simpleName}") }

    protected fun file(name: String): File {
        return File(plugin.dataFolder, name)
    }

    override fun onLoad(context: MutablePropertyMap) {
        this.context = context
    }
}