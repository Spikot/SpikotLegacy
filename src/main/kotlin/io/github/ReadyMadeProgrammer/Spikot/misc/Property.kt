package io.github.ReadyMadeProgrammer.Spikot.misc

interface Property<T> {
    val key: String
}

class SimpleProperty<T>(
    override val key: String
) : Property<T>

class SimpleFlagProperty(
    override val key: String
) : Property<Unit>