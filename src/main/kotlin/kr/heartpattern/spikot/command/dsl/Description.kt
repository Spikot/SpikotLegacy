package kr.heartpattern.spikot.command.dsl

import kr.heartpattern.spikot.command.*
import kr.heartpattern.spikot.command.internal.CommandNode
import kr.heartpattern.spikot.utils.findAnnotations
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

abstract class Description {
    var name: Set<String> = emptySet()
    var usage: String = ""
    var help: String = ""
    var childs: Set<CommandNode> = emptySet()
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
        childs = (enclosingClass.findAnnotations<Child>()).map { it.child }.map { CommandNode(it) }.toSet()
    }

    fun childs(builder: ChildBuilder.() -> Unit) {
        val childBuilder = ChildBuilder()
        childBuilder.builder()
        childs = childBuilder.set
    }

    fun completer(completer: CommandContext.() -> List<String>) {
        this.completer = completer
    }
}