package io.github.ReadyMadeProgrammer.Spikot.reflections

internal val constructorNmsNbtTagCompound = classNbtTagCompound.getDeclaredConstructor()

inline class NmsItemStack(val handle: Any)
inline class NmsNbtTagCompound(val handle: Any) {
    constructor() : this(constructorNmsNbtTagCompound.newInstance())
}