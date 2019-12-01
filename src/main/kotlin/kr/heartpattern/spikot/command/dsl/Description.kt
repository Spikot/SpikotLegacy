package kr.heartpattern.spikot.command.dsl

import kr.heartpattern.spikot.command.*
import kr.heartpattern.spikot.utils.findAnnotations
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation

abstract class Description {
    var name: Set<String> = emptySet()
    var usage: String = ""
    var help: String = ""
    var childDescriptions: Map<KClass<out AbstractCommand>, Description> = emptyMap()
        internal set
    var childClasses: Set<KClass<out AbstractCommand>> = emptySet()
        internal set
    var completer: CommandContext.() -> List<String> = { emptyList() }
        internal set

    constructor(initializer: Description.() -> Unit) {
        initializer()
    }

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

    fun childs(builder: ChildBuilder.() -> Unit) {
        val childBuilder = ChildBuilder()
        childBuilder.builder()
        childClasses = childBuilder.set
        childDescriptions = childClasses
            .asSequence()
            .map { it to (it.companionObjectInstance as Description) }
            .toMap()
    }

    fun completer(completer: CommandContext.() -> List<String>) {
        this.completer = completer
    }
}