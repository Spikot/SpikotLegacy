@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import org.bukkit.command.CommandSender
import kotlin.math.min

abstract class CommandHandler {
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var context: CommandContext
    protected lateinit var plugin: Spikot
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
            val realIndex = index + depth
            if (context.args.size <= realIndex) {
                null
            } else {
                context.args[realIndex]
            }
        }
        delegates += delegate
        return delegate
    }

    fun remains(start: Int): NotNullVerboseProperty<List<String>> {
        val delegate = NotNullVerboseProperty({ context.commandSender }) {
            if (context.args.size <= start + depth)
                emptyList()
            else
                context.args.subList(start + depth, context.args.size)
        }
        delegates += delegate
        return delegate
    }

    fun args(range: IntRange): NotNullVerboseProperty<List<String>> {
        val delegate = NotNullVerboseProperty({ context.commandSender }) {
            val realRange = IntRange(range.start + depth, range.endInclusive + depth)
            if (context.args.size <= range.first) {
                emptyList()
            } else {
                context.args.subList(realRange.first, min(realRange.last + 1, context.args.size))
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
        if (e is CastException || e is VerifyException) {
            context.commandSender.sendMessage(e.message)
        } else {
            throw e
        }
    }
}

class NoSuchCommandException : RuntimeException()