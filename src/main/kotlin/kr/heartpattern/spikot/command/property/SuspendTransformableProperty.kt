package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.CommandContext

interface SuspendTransformableProperty<T>: TransformableProperty<T>{
    fun <R> suspendTransform(transformer: suspend CommandContext.(T)->R): SuspendCommandProperty<R>
    fun suspendValidate(validator: suspend TransformerContext.(T)->Boolean): SuspendCommandProperty<T>
}