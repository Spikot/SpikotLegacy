/*
 * Copyright 2020 HeartPattern
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

package kr.heartpattern.spikot.command.dsl

import kr.heartpattern.spikot.command.*
import kr.heartpattern.spikot.utils.findAnnotations
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation

/**
 * Describe command information
 */
abstract class Description {
    /**
     * Set of name and alias
     */
    var name: Set<String> = emptySet()

    /**
     * Usage message
     */
    var usage: String = ""

    /**
     * Help message
     */
    var help: String = ""

    /**
     * Map of child descriptions
     */
    var childDescriptions: Map<KClass<out AbstractCommand>, Description> = emptyMap()
        internal set

    /**
     * Set of child classes
     */
    var childClasses: Set<KClass<out AbstractCommand>> = emptySet()
        internal set

    /**
     * Tab completer
     */
    var completer: CommandContext.() -> List<String> = { emptyList() }
        internal set

    /**
     * Create Description from configuring lambda
     * @param initializer lambda which configure description
     */
    constructor(initializer: Description.() -> Unit) {
        initializer()
    }

    /**
     * Create Description from class and its annotation
     * @param enclosingClass Enclosing AbstractCommand class
     */
    constructor(enclosingClass: KClass<*>) {
        name = enclosingClass.findAnnotation<Name>()?.name?.toSet() ?: emptySet()
        usage = enclosingClass.findAnnotation<Usage>()?.usage ?: ""
        help = (enclosingClass.findAnnotation<Help>())?.help ?: ""
        childClasses = (enclosingClass.findAnnotations<Child>()).map { it.child }.toSet()
        childDescriptions = childClasses
            .asSequence()
            .map { it to (it.companionObjectInstance as Description) }
            .toMap()
    }

    /**
     * Configure child commands with ChildBuilder
     * @param builder ChildBuilder configure lambda
     */
    fun childs(builder: ChildBuilder.() -> Unit) {
        val childBuilder = ChildBuilder()
        childBuilder.builder()
        childClasses = childBuilder.set
        childDescriptions = childClasses
            .asSequence()
            .map { it to (it.companionObjectInstance as Description) }
            .toMap()
    }

    /**
     * Set command completer
     * @param completer Command completer
     */
    fun completer(completer: CommandContext.() -> List<String>) {
        this.completer = completer
    }
}