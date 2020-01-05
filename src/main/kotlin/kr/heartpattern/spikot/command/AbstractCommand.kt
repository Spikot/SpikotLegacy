package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.dsl.Description
import kr.heartpattern.spikot.command.property.CommandProperty
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.reflect.full.companionObjectInstance

/**
 * Representation one command
 */
abstract class AbstractCommand {
    internal lateinit var context: CommandContext
    internal val properties = LinkedList<CommandProperty<*>>()

    /**
     * Plugin which register this command
     */
    val plugin: SpikotPlugin by lazy { context.plugin }

    /**
     * Description of this command
     */
    val description: Description = this::class.companionObjectInstance as Description

    /**
     * Property that return command sender
     * @return CommandProperty return CommandSender of this command
     */
    fun sender(): CommandProperty<CommandSender> {
        val property = CommandProperty(-2) { context.sender }
        properties += property
        return property
    }

    /**
     * Property that return command label
     * @return CommandProperty return label of this command
     */
    fun label(): CommandProperty<String> {
        val property = CommandProperty(-1) { context.label }
        properties += property
        return property
    }

    /**
     * Property that return argument.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1 in "/root sub arg1 arg2" is 0.
     * @param position Position of argument
     * @return CommandProperty which return argument. If argument is not presented, property return null.
     */
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

    /**
     * Property that return list of arguments.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1, arg2 in "/root sub arg1 arg2" is 0, 1
     * @param range Range of position of argument
     * @return CommandProperty which return list of argument. List size can be different from size of range if argument
     * is not fully given.
     */
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

    /**
     * Property that return all remaining argument after given position.
     * Position start from 0, and position does not count sub command argument.
     * For example, this abstract command's alias is sub and child of /root,
     * then position of arg1 in "/root sub arg1 arg2" is 0.
     * @param start First position of remaining argument
     * @return CommandProperty which return list of remaining argument.
     */
    fun remains(start: Int): CommandProperty<List<String>> = args(start..Int.MAX_VALUE)
}