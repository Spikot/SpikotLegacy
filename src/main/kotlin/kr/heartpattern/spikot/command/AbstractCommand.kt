package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.dsl.Description
import kr.heartpattern.spikot.command.property.CommandProperty
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.reflect.full.companionObjectInstance

abstract class AbstractCommand {
    internal lateinit var context: CommandContext
    internal val properties = LinkedList<CommandProperty<*>>()
    val plugin: SpikotPlugin by lazy { context.plugin }
    val description: Description = this::class.companionObjectInstance as Description

    fun sender(): CommandProperty<CommandSender> {
        val property = CommandProperty(-1) { context.sender }
        properties += property
        return property
    }

    fun arg(position: Int): CommandProperty<String?> {
        val property = CommandProperty(position) {
            if (context.args.size > position)
                context.args[position]
            else
                null
        }
        properties += property
        return property
    }

    fun args(range: IntRange): CommandProperty<List<String>> {
        val property = CommandProperty(range.first) {
            when {
                context.args.size > range.last -> context.args.subList(range.first, range.last + 1)
                context.args.size > range.first -> context.args.subList(range.first, context.args.size)
                else -> emptyList()
            }
        }
        properties += property
        return property
    }

    fun remains(start: Int): CommandProperty<List<String>> = args(start..Int.MAX_VALUE)
}