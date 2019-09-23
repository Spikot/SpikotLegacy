package io.github.ReadyMadeProgrammer.Spikot.misc

interface Property<T> {
    val key: String
}

abstract class SimpleProperty<T>(
    override val key: String
) : Property<T>

abstract class SimpleFlagProperty(
    override val key: String
) : Property<Unit>