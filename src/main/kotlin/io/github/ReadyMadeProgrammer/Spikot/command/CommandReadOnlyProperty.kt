package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.utils.Cache
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Verifier<T> = (T) -> Boolean
typealias Cast<T, R> = (T) -> (R)

class NotNullCommandReadOnlyProperty<T : Any> internal constructor(
        source: () -> T)
    : ReadOnlyProperty<CommandHandler, T> {
    private val value = Cache(source)
    override fun getValue(thisRef: CommandHandler, property: KProperty<*>) = value()

    fun <R : Any> cast(message: String = "Cannot cast", caster: Cast<in T, out R>) = NotNullCommandReadOnlyProperty {
        try {
            caster(value())
        } catch (e: Throwable) {
            throw CastException(message, e)
        }
    }

    fun verify(message: String = "Verify error", verifier: Verifier<in T>) = NotNullCommandReadOnlyProperty {
        val v = value()
        if (!verifier(v)) {
            throw VerifyException(message)
        }
        v
    }

    fun nullable() = NullableCommandReadOnlyProperty { value() }
}

class NullableCommandReadOnlyProperty<T : Any> internal constructor(
        source: () -> T?)
    : ReadOnlyProperty<CommandHandler, T?> {
    private val value = Cache(source)
    override fun getValue(thisRef: CommandHandler, property: KProperty<*>) = value()

    fun <R : Any> cast(message: String = "Cannot cast", caster: Cast<in T, out R?>) = NullableCommandReadOnlyProperty {
        val v = value()
        if (v == null) null
        else {
            try {
                caster(v)
            } catch (e: Throwable) {
                throw CastException(message, e)
            }
        }
    }

    fun verify(message: String = "Verify error", verifier: Verifier<in T>) = NullableCommandReadOnlyProperty {
        val v = value()
        if (v != null) {
            if (!verifier(v)) {
                throw VerifyException(message)
            }
        }
    }

    fun notNull(message: String) = NotNullCommandReadOnlyProperty {
        value() ?: throw CastException(message)
    }
}

class VerifyException : RuntimeException {
    constructor() : super()
    constructor(p0: String?) : super(p0)
    constructor(p0: String?, p1: Throwable?) : super(p0, p1)
    constructor(p0: Throwable?) : super(p0)
    constructor(p0: String?, p1: Throwable?, p2: Boolean, p3: Boolean) : super(p0, p1, p2, p3)
}

class CastException : RuntimeException {
    constructor() : super()
    constructor(p0: String?) : super(p0)
    constructor(p0: String?, p1: Throwable?) : super(p0, p1)
    constructor(p0: Throwable?) : super(p0)
    constructor(p0: String?, p1: Throwable?, p2: Boolean, p3: Boolean) : super(p0, p1, p2, p3)
}