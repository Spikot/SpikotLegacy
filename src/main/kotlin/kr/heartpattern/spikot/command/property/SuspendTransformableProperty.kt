/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.CommandContext

/**
 * Transformable property delegate which can use suspend function in lambda.
 * @param T Type of property
 */
interface SuspendTransformableProperty<T> : TransformableProperty<T> {
    /**
     * Transform this property with suspend transformer
     * @param R Destination type
     * @param transformer suspend lambda that transform this property
     * @return Transformed property
     */
    fun <R> suspendTransform(transformer: suspend TransformerContext.(T) -> R): SuspendTransformableProperty<R>

    /**
     * Validate this property with suspend validator
     * @param validator suspend lambda that validate this property
     * @return This property
     */
    fun suspendValidate(validator: suspend CommandContext.(T)->Boolean): SuspendTransformableProperty<T>
}