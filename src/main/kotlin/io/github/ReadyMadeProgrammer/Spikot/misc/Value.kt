package io.github.ReadyMadeProgrammer.Spikot.misc

interface Value<T> {
    companion object {
        operator fun <T> invoke(value: T): Value<T> {
            return SimpleValue(value)
        }
    }

    var value: T
    fun <U> get(): U {
        @Suppress("UNCHECKED_CAST")
        return value as U
    }
}

open class SimpleValue<T>(private var _value: T) : Value<T> {
    override var value: T
        get() = _value
        set(value) {
            _value = value
        }
}

class ObservableValue<T>(value: T, private val observer: (T) -> Unit) : SimpleValue<T>(value) {
    override var value: T
        get() = super.value
        set(value) {
            observer(value)
            super.value = value
        }
}

class VetoableValue<T>(value: T, private val observer: (T) -> Boolean) : SimpleValue<T>(value) {
    override var value: T
        get() = super.value
        set(value) {
            if (observer(value)) {
                super.value = value
            }
        }
}