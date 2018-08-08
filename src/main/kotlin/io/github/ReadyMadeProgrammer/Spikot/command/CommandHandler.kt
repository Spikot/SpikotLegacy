package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.modules.Component
import org.bukkit.command.CommandSender
import kotlin.math.min

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class RootCommand

@Suppress("UNUSED_PARAMETER", "unused")
abstract class CommandHandler(private val context: CommandContext) : Component {
    abstract val info: CommandInfoBuilder
    protected fun info(build: CommandInfoBuilder.() -> Unit): CommandInfoBuilder {
        val builder = CommandInfoBuilder()
        builder.build()
        return builder
    }

    abstract fun execute()
    protected fun arg(i: Int): NullableCommandReadOnlyProperty<String> {
        return NullableCommandReadOnlyProperty {
            if (context.argument.size <= i)
                null
            else
                context.argument[i]
        }
    }

    protected fun args(range: IntRange): NullableCommandReadOnlyProperty<List<String>> {
        return NullableCommandReadOnlyProperty {
            if (range.start >= 0 && range.start <= range.endInclusive)
                context.argument.subList(range.start, min(range.endInclusive + 1, context.argument.size))
            else listOf<String>()
        }
    }

    protected fun flag(vararg s: String): NullableCommandReadOnlyProperty<String> {
        TODO("Implement later")
    }

    protected fun option(vararg s: String): NullableCommandReadOnlyProperty<Boolean> {
        TODO("Implement later")
    }

    protected fun list(vararg s: String): NullableCommandReadOnlyProperty<List<String>> {
        TODO("Implement later")
    }

    protected fun sender(): NotNullCommandReadOnlyProperty<CommandSender> {
        return NotNullCommandReadOnlyProperty { context.sender }
    }
}