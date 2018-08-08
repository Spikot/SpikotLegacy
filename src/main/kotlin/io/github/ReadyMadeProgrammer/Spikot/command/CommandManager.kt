package io.github.ReadyMadeProgrammer.Spikot.command

import com.google.common.collect.HashMultimap
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.jvm.kotlinFunction

@Suppress("unused", "UNUSED_PARAMETER")
object CommandManager {
    private val rootCommands: ConcurrentHashMap<String, CommandGraph<*>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    internal fun addCommand(clazz: Class<*>) {
        addCommand(CommandGraph.invoke(clazz.getConstructor(CommandContext::class.java).kotlinFunction as (CommandContext) -> CommandHandler))
    }

    internal fun addCommand(graph: CommandGraph<*>) {
        graph.info.name.forEach {
            rootCommands[it] = graph
        }
    }

    internal fun invoke(commandSender: CommandSender, command: Command, label: String, args: Array<String>) {
        val commandHandler = rootCommands[label]
        if (commandHandler == null) {
            throw  IllegalArgumentException("Command Not Found")
        } else {
            commandHandler.invoke(CommandContext(HashMultimap.create(), args.asLinkedList(), commandSender))
        }
    }

    internal fun complete(commandSender: CommandSender, command: Command, label: String, args: Array<String>): Set<String> {
        val commandHandler = rootCommands[label]
        return if (commandHandler == null) {
            Collections.emptySet()
        } else {
            commandHandler.complete(CommandContext(HashMultimap.create(), args.asLinkedList(), commandSender))
        }
    }

    private fun <T> Array<T>.asLinkedList(): LinkedList<T> {
        val linkedList = LinkedList<T>()
        this.forEach {
            linkedList.add(it)
        }
        return linkedList
    }
}