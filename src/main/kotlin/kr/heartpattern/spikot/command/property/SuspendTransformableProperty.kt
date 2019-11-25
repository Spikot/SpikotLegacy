package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.CommandContext
import javax.xml.transform.Transformer

interface SuspendTransformableProperty<T>: TransformableProperty<T>{
    fun <R> suspendTransform(transformer: suspend TransformerContext.(T)->R): SuspendCommandProperty<R>
    fun suspendValidate(validator: suspend CommandContext.(T)->Boolean): SuspendCommandProperty<T>
}