package io.github.ReadyMadeProgrammer.Spikot.command

import kotlin.reflect.KClass

@DslMarker
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandDsl

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class RootCommand

@CommandDsl
open class CommandInfo(build: CommandInfo.() -> Unit) {
    var name: Set<String> = emptySet()
    var usage: String = ""
    var help: String = ""
    internal var completer: TabCompleter.() -> List<String> = { emptyList() }
    internal var _childs: Set<KClass<out CommandHandler>> = emptySet()
    fun childs(build: ChildsBuilder.() -> Unit) {
        val childBuilder = ChildsBuilder()
        childBuilder.build()
        _childs = childBuilder.childs
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