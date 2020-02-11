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

package kr.heartpattern.spikot.plugin

import kotlin.reflect.KClass

/**
 * Annotate annotation to be record in compile time.
 * Annotation which annotated with this can be found by SpikotPluginManager.forEachAnnotation
 * @param impl Array of class which should be implemented by class that annotated with this annotation.
 * Validation of it is done in compile time and occur compile error.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class FindAnnotation(
    val impl: Array<KClass<*>> = []
)