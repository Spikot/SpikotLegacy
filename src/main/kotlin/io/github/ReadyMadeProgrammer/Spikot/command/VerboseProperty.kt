package io.github.ReadyMadeProgrammer.Spikot.command

import org.bukkit.command.CommandSender
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Verifier<T> = (T) -> Boolean
typealias Caster<T, R> = (T) -> R

sealed class VerboseProperty {
    internal var next: VerboseProperty? = null
}

class NotNullVerboseProperty<T : Any>(val commandSender: () -> CommandSender, val value: () -> T) : ReadOnlyProperty<Any?, T>, VerboseProperty() {
    private val verifier = mutableListOf<Pair<Verifier<T>, String?>>()
    private var verified = false
    private lateinit var cached: T
    internal fun get(): T {
        if (!verified) {
            if (!this::cached.isInitialized) {
                this.cached = value()
            }
            verifier.forEach {
                if (!it.first(cached)) {
                    throw VerifyException(it.second, commandSender())
                }
            }
            verified = true
        }
        return cached
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

    fun verify(message: String? = null, verifier: Verifier<T>): NotNullVerboseProperty<T> {
        this.verifier += Pair(verifier, message)
        return this
    }

    fun <R : Any> cast(message: String? = null, caster: Caster<T, R>): NotNullVerboseProperty<R> {
        next = NotNullVerboseProperty(commandSender) {
            try {
                caster(get())
            } catch (e: Exception) {
                throw CastException(message, commandSender(), e)
            }
        }
        return next as NotNullVerboseProperty<R>
    }

    fun nullable(): NullableVerboseProperty<T> {
        next = NullableVerboseProperty(commandSender, value)
        return next as NullableVerboseProperty<T>
    }
}

class NullableVerboseProperty<T : Any>(val commandSender: () -> CommandSender, val value: () -> T?) : ReadOnlyProperty<Any?, T?>, VerboseProperty() {
    private val verifier = mutableListOf<Pair<Verifier<T>, String?>>()
    private var verified = false
    private var cached: T? = null
    private var caching = false

    internal fun get(): T? {
        if (!verified) {
            if (!caching) {
                cached = value()
            }
            val v = cached
            if (v != null) {
                verifier.forEach {
                    if (!it.first(v)) {
                        throw VerifyException(it.second, commandSender())
                    }
                }
            }
            verified = true
        }
        return value()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = get()

    fun verify(message: String? = null, verifier: Verifier<T>): NullableVerboseProperty<T> {
        this.verifier += Pair(verifier, message)
        return this
    }

    fun <R : Any> cast(message: String? = null, caster: Caster<T, R?>): NullableVerboseProperty<R> {
        next = NullableVerboseProperty(commandSender) {
            try {
                val v = get()
                if (v == null)
                    null
                else
                    caster(v)
            } catch (e: Exception) {
                throw CastException(message, commandSender(), e)
            }
        }
        return next as NullableVerboseProperty<R>
    }

    fun notNull(message: String? = null): NotNullVerboseProperty<T> {
        next = NotNullVerboseProperty(commandSender) {
            if (get() == null) {
                throw VerifyException(message, commandSender())
            } else {
                get()!!
            }
        }
        return next as NotNullVerboseProperty<T>
    }
}

class VerifyException(message: String?, val commandSender: CommandSender) : RuntimeException(message)
class CastException(message: String?, val commandSender: CommandSender, exception: Exception) : RuntimeException(message, exception)