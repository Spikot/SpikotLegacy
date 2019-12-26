package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import kotlin.properties.ReadOnlyProperty

/**
 * Transformable property delegate
 * @param T Type of property
 */
interface TransformableProperty<T> : ReadOnlyProperty<AbstractCommand, T> {
    /**
     * Whether this property is initialized
     */
    val isInitialized: Boolean

    /**
     * Transform this property
     * @param R Destination target
     * @param transformer lambda that transform this property
     * @return Transformed property
     */
    fun <R> transform(transformer: TransformerContext.(T) -> R): TransformableProperty<R>

    /**
     * Validate this property
     * @param validator lambda that validate this property
     * @return This property
     */
    fun validate(validator: CommandContext.(T) -> Boolean): TransformableProperty<T>
}