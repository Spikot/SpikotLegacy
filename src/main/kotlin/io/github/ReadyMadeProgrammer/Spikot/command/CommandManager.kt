@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

object CommandManager {
    private val commandHolders: MutableSet<CommandHolder> = mutableSetOf()
    private val commandNames = LinkedList<String>()
    internal fun add(rootCommand: KClass<out CommandHandler>) {
        val commandHolder = CommandHolder(rootCommand)
        commandHolders += commandHolder
        commandHolder.name.forEach {
            commandNames.add(it)
        }
    }

    internal fun invoke(plugin: Spikot, commandSender: CommandSender, command: Command, label: String, args: Array<String>) {
        val root = commandHolders.find { holder -> holder.name.any { it.equals(label, true) } }
        if (root == null) {
            plugin.onCommandException(commandSender, command, label, args.asList(), NoSuchCommandException())
        } else {
            root.invoke(plugin, CommandContext(commandSender, command, label, args.asList()), 0)
        }
    }

    internal fun complete(plugin: Spikot, commandSender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val root = commandHolders.find { holder -> holder.name.any { it.equals(label, true) } }
        return root?.complete(plugin, CommandContext(commandSender, command, label, args.asList()), 0) ?: commandNames
    }
}

class CommandHolder(private val commandHandler: KClass<out CommandHandler>) {
    internal val name: Set<String>
    private val usage: String
    private val help: String
    private val child: Set<CommandHolder>
    private val childNames = LinkedList<String>()
    private val completer: TabCompleter.() -> List<String>

    init {
        val commandInfo = commandHandler.companionObjectInstance
        if (commandInfo == null || commandInfo !is CommandInfo) {
            throw IllegalArgumentException("CommandHandler must have CommandInfo Companion Object")
        }
        name = commandInfo.name.map { it.toLowerCase() }.toSet()
        help = commandInfo.help
        usage = commandInfo.usage
        child = commandInfo.childs.asSequence().map { CommandHolder(it) }.toSet()
        completer = try {
            commandInfo.completer
        } catch (e: Exception) {
            { emptyList() }
            //Do nothing
        }
        commandInfo.name.forEach {
            println("Register Command: $it")
        }
        child.forEach { ch ->
            ch.name.forEach {
                childNames.add(it)
            }
        }
    }

    internal fun invoke(plugin: Spikot, commandContext: CommandContext, depth: Int) {
        val subCommand = if (commandContext.args.size <= depth) {
            null
        } else {
            child.find { it.name.contains(commandContext.args[depth].toLowerCase()) }
        }
        if (subCommand == null) {
            try {
                val handler = commandHandler.createInstance()
                try {
                    handler.initialize(commandContext, plugin, depth)
                    handler.execute()
                } catch (exception: NoSuchCommandException) {
                    usage(commandContext, depth)
                } catch (exception: Exception) {
                    handler.onException(exception)
                }
            } catch (exception: Exception) {
                plugin.onCommandException(commandContext.commandSender, commandContext.command, commandContext.label, commandContext.args, exception)
            }
        } else {
            subCommand.invoke(plugin, commandContext, depth + 1)
        }
    }

    internal fun complete(plugin: Spikot, commandContext: CommandContext, depth: Int): List<String> {
        val subCommand = if (commandContext.args.size <= depth) {
            null
        } else {
            child.find { it.name.contains(commandContext.args[depth]) }
        }
        return if (subCommand == null) {
            val complete = TabCompleter(commandContext.commandSender, commandContext.args, if (commandContext.args.size == depth - 1) null else commandContext.args.last())
            complete.completer() + childNames.filter { it.startsWith(commandContext.args[depth], ignoreCase = true) }
        } else {
            subCommand.complete(plugin, commandContext, depth + 1)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    internal fun usage(commandContext: CommandContext, depth: Int) {
        val subCommand = child.find { it.name.contains(commandContext.args[depth]) }
        if (subCommand == null) {
            commandContext.commandSender.sendMessage(usage)
            child.forEach {
                commandContext.commandSender.sendMessage(it.usage)
            }
        } else {
            subCommand.usage(commandContext, depth + 1)
        }
    }

    internal fun help(commandContext: CommandContext, depth: Int) {
        val subCommand = child.find { it.name.contains(commandContext.args[depth]) }
        if (subCommand == null) {
            commandContext.commandSender.sendMessage(help)
        } else {
            subCommand.usage(commandContext, depth + 1)
        }
    }
}