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
import kr.heartpattern.spikot.command.PropertyNotInitializedException
import kr.heartpattern.spikot.command.ValidationException
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import java.util.*
import kotlin.reflect.KProperty

/**
 * Command property which represent some information of command.
 * SuspendCommandProperty can use suspended function in transform and validate lambda.
 */
class SuspendCommandProperty<T>(val pos: Int, val value: suspend TransformerContext.() -> T) : SuspendTransformableProperty<T> {
    private var cache: Option<T> = None
    private val suspendValidators: MutableList<suspend CommandContext.(T) -> Boolean> = LinkedList()
    private val validators: MutableList<CommandContext.(T) -> Boolean> = LinkedList()
    private val childs: MutableList<SuspendCommandProperty<*>> = LinkedList()

    internal suspend fun initialize(context: CommandContext) {
        if (cache == None) {
            val transformerContext = TransformerContext(pos, context)
            val fetch = transformerContext.value()
            for (validator in validators)
                if (!context.validator(fetch))
                    throw ValidationException(pos)

            for (validator in suspendValidators)
                if (!context.validator(fetch))
                    throw ValidationException(pos)
            cache = Just(fetch)
        }
        for (child in childs) {
            child.initialize(context)
        }
    }

    override val isInitialized: Boolean
        get() = cache is Just<T>

    override fun getValue(thisRef: AbstractCommand, property: KProperty<*>): T {
        return (cache as? Just<T>)?.value ?: throw PropertyNotInitializedException(property)
    }

    override fun <R> transform(transformer: TransformerContext.(T) -> R): SuspendCommandProperty<R> {
        val child = SuspendCommandProperty<R>(pos) { transformer((cache as Just<T>).value) }
        childs += child
        return child
    }

    override fun validate(validator: CommandContext.(T) -> Boolean): SuspendCommandProperty<T> {
        validators += validator
        return this
    }

    override fun <R> suspendTransform(transformer: suspend TransformerContext.(T) -> R): SuspendCommandProperty<R> {
        val child = SuspendCommandProperty<R>(pos) { transformer((cache as Just<T>).value) }
        childs += child
        return child
    }

    override fun suspendValidate(validator: suspend CommandContext.(T) -> Boolean): SuspendCommandProperty<T> {
        suspendValidators += validator
        return this
    }
}