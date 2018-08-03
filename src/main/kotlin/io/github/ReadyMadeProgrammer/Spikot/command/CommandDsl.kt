package io.github.ReadyMadeProgrammer.Spikot.command

import java.util.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class CommandDsl

@CommandDsl
class CommandInfoBuilder {
    var name: Set<String> = setOf()
    var help: String = ""
    var description: String = ""
    var completer: (CommandContext) -> Set<String> = { Collections.emptySet() }
    internal val child = ChildCommandBuilder()
    fun childs(builder: ChildCommandBuilder.() -> Unit) {
        child.builder()
    }

    internal val argsInfo = mutableSetOf<ArgumentInfo>()
}

@CommandDsl
class ChildCommandBuilder {
    internal val childs = mutableSetOf<(CommandContext) -> CommandHandler>()
    operator fun <T : CommandHandler> ((CommandContext) -> T).unaryPlus() {
        childs.add(this)
    }
}

internal data class ArgumentInfo(var type: ArgumentType, var name: String?, var help: String?, val flag: Set<String>)

enum class ArgumentType {
    FLAG, ARGUMENT, OPTIONAL, HOLDER
}