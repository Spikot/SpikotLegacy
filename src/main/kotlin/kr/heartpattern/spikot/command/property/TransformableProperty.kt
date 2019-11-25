package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import kotlin.properties.ReadOnlyProperty

interface TransformableProperty<T> : ReadOnlyProperty<AbstractCommand, T> {
    val isInitialized: Boolean
    fun <R> transform(transformer: TransformerContext.(T) -> R): TransformableProperty<R>
    fun validate(validator: CommandContext.(T) -> Boolean): TransformableProperty<T>
}