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

/**
 * Interface of module
 */
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

    /**
     * Module context
     */
    val context: MutablePropertyMap

    /**
     * Invoke when module is in load state to set context
     */
    fun onLoad(context: MutablePropertyMap)

    /**
     * Invoke when module is on enable state
     */
    fun onEnable() {}

    /**
     * Invoke when module is on disable state
     */
    fun onDisable() {}
}

/**
 * Partially implemented IModule with useful methods
 */
abstract class AbstractModule : IModule {
    final override lateinit var context: MutablePropertyMap

    /**
     * Delegate module context property lazily
     * @param prop Property to get
     * @return ReadOnlyProperty
     */
    protected fun <T> contextDelegate(prop: Property<T>): ReadOnlyProperty<Any, T?> = object : ReadOnlyProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return context[prop]
        }
    }

    /**
     * Delegate module context flag property lazily
     * @param prop Property to get
     * @return ReadOnlyProperty
     */
    protected fun contextDelegate(prop: FlagProperty): ReadOnlyProperty<Any, Boolean> = object : ReadOnlyProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return context[prop]
        }
    }

    /**
     * Delegate mutable module context property lazily
     * @param prop Property to get
     * @return ReadWriteProperty
     */
    protected fun <T> mutableContextDelegate(prop: MutableProperty<T>): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return context[prop]
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            context[prop] = value
        }
    }

    /**
     * Delegate mutable module context flag property lazily
     * @param prop Property to get
     * @return ReadWriteProperty
     */
    protected fun mutableContextDelegate(prop: MutableFlagProperty): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return context[prop]
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            context[prop] = value
        }
    }

    /**
     * Plugin which define this module
     */
    protected val plugin: SpikotPlugin by contextDelegate(IModule.PluginProperty).nonnull()

    /**
     * Logger for this module
     */
    protected val logger: KLogger by lazy { KotlinLogging.logger(this@AbstractModule.javaClass.name) }

    /**
     * Create new file in data folder
     * @param name Name of file
     * @return File instance. It's parent directory's existence is not guaranteed.
     */
    protected fun file(name: String): File {
        return File(plugin.dataFolder, name)
    }

    override fun onLoad(context: MutablePropertyMap) {
        this.context = context
    }
}