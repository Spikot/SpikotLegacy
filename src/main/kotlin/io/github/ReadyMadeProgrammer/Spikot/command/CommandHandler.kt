package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import org.bukkit.command.CommandSender
import kotlin.math.min

abstract class CommandHandler {
    lateinit var context: CommandContext
    lateinit var plugin: Spikot
    private var depth: Int = 0
    private val delegates = HashSet<VerboseProperty>()

    internal fun initialize(context: CommandContext, plugin: Spikot, depth: Int) {
        this.context = context
        this.plugin = plugin
        this.depth = depth
        delegates.forEach { p ->
            var property = p
            while (property.next != null) {
                property = property.next!!
            }
            if (property is NullableVerboseProperty<*>) {
                property.get()
            } else if (property is NotNullVerboseProperty<*>) {
                property.get()
            }
        }
    }

    fun arg(index: Int): NullableVerboseProperty<String> {
        val delegate = NullableVerboseProperty({ context.commandSender }) {
            val _index = index + depth
            if (context.args.size <= _index) {
                null
            } else {
                context.args[_index]
            }
        }
        delegates += delegate
        return delegate
    }

    fun args(range: IntRange): NotNullVerboseProperty<List<String>> {
        val delegate = NotNullVerboseProperty({ context.commandSender }) {
            val _range = IntRange(range.start + depth, range.endInclusive + depth)
            if (context.args.size <= range.first) {
                emptyList()
            } else {
                context.args.subList(_range.first, min(_range.last + 1, context.args.size))
            }
        }
        delegates += delegate
        return delegate
    }

    fun sender(): NotNullVerboseProperty<CommandSender> {
        val delegate = NotNullVerboseProperty({ context.commandSender }) { context.commandSender }
        delegates += delegate
        return delegate
    }

    open fun execute() {
        throw NoSuchCommandException()
    }

    open fun onException(e: Exception) {
        plugin.onCommandException(context.commandSender, context.command, context.label, context.args, e)
    }
}

class NoSuchCommandException : RuntimeException()