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

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.CommandContext
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