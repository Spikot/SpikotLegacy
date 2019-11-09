@file:Suppress("unused")

package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

@DslMarker
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandDsl

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation
annotation class RootCommand(val prefix: String)

@CommandDsl
open class CommandInfo(build: CommandInfo.() -> Unit) {
    var name: Set<String> = emptySet()
    var usage: String = ""
    var help: String = ""
    internal var completer: TabCompleter.() -> List<String> = { emptyList() }
    internal var childs: Set<KClass<out CommandHandler>> = emptySet()
    fun childs(build: ChildsBuilder.() -> Unit) {
        val childBuilder = ChildsBuilder()
        childBuilder.build()
        childs = childBuilder.childs
    }

    fun complete(completer: TabCompleter.() -> List<String>) {
        this.completer = completer
    }

    init {
        this.build()
    }
}

@CommandDsl
class ChildsBuilder {
    @PublishedApi
    internal var childs = mutableSetOf<KClass<out CommandHandler>>()

    inline fun <reified T : CommandHandler> add() {
        childs.add(T::class)
    }
}