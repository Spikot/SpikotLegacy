package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import kotlin.properties.ReadOnlyProperty

interface TransformableProperty<T> : ReadOnlyProperty<Command, T> {
    val isInitialized: Boolean
    fun <R> transform(transformer: CommandContext.(T) -> R): TransformableProperty<R>
    fun validate(validator: TransformerContext.(T) -> Boolean): TransformableProperty<T>
}