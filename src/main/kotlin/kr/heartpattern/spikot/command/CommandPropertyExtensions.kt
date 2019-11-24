package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.command.property.CommandProperty
import kr.heartpattern.spikot.command.property.SuspendCommandProperty

fun <T : Any> CommandProperty<T?>.nonnull(): CommandProperty<T> {
    return transform { it!! }
}

fun <T : Any> CommandProperty<T>.nullable(): CommandProperty<T?> {
    @Suppress("UNCHECKED_CAST")
    return this as CommandProperty<T?>
}

fun <T : Any> SuspendCommandProperty<T?>.nonnull(): SuspendCommandProperty<T> {
    return transform { it!! }
}

fun <T : Any> SuspendCommandProperty<T>.nullable(): SuspendCommandProperty<T?> {
    @Suppress("UNCHECKED_CAST")
    return this as SuspendCommandProperty<T?>
}