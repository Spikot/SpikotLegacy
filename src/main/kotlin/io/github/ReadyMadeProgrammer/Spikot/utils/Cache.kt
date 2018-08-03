package io.github.ReadyMadeProgrammer.Spikot.utils

class Cache<out T>(private val source: () -> T) {
    private var internalValue: CacheHolder<T> = CacheHolder.Invalid
    val isValid: Boolean
        get() = internalValue == CacheHolder.Invalid

    operator fun invoke() = value
    val value: T
        get() {
            if (internalValue == CacheHolder.Invalid) {
                internalValue = CacheHolder.Value(source())
            }
            return (internalValue as CacheHolder.Value<T>).value
        }

    fun invalidate() {
        internalValue = CacheHolder.Invalid
    }

    private sealed class CacheHolder<out T> {
        object Invalid : CacheHolder<Nothing>()
        class Value<out T>(val value: T) : CacheHolder<T>()
    }
}