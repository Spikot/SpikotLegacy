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

package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

/**
 * Annotate adapter implementation
 * @param target Implemented adapter class
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@FindAnnotation(impl = [IAdapter::class])
annotation class Adapter(
    val target: KClass<*> = Nothing::class
)

/**
 * Adapter implementation
 */
interface IAdapter : IModule