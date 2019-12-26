package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.CommandContext
import javax.xml.transform.Transformer

/**
 * Transformable property delegate which can use suspend function in lambda.
 * @param T Type of property
 */
interface SuspendTransformableProperty<T>: TransformableProperty<T>{
    /**
     * Transform this property with suspend transformer
     * @param R Destination type
     * @param transformer suspend lambda that transform this property
     * @return Transformed property
     */
    fun <R> suspendTransform(transformer: suspend TransformerContext.(T)->R): SuspendTransformableProperty<R>

    /**
     * Validate this property with suspend validator
     * @param validator suspend lambda that validate this property
     * @return This property
     */
    fun suspendValidate(validator: suspend CommandContext.(T)->Boolean): SuspendTransformableProperty<T>
}