@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.Spikot
import io.github.ReadyMadeProgrammer.Spikot.module.*
import io.github.ReadyMadeProgrammer.Spikot.plugin.SpikotPluginManager
import io.github.ReadyMadeProgrammer.Spikot.utils.catchAll
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.math.min
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

@Module(loadOrder = LoadOrder.API)
object CommandManager : AbstractModule(), TabExecutor {
    private val commandHolders: MutableSet<CommandHolder> = mutableSetOf()
    private val commandNames = LinkedList<String>()
    private val constructor = PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
    private fun create(name: String, owner: Plugin): PluginCommand {
        constructor.isAccessible = true
        return constructor.newInstance(name, owner)
    }

    override fun onEnable() {
        val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        val value = field[Bukkit.getServer()] as CommandMap
        field.isAccessible = false
        SpikotPluginManager.forEach<RootCommand> { plugin, kclass ->
            onDebug {
                logger.info("Find command: ${kclass.simpleName}")
            }
            if (!kclass.canLoad()) return@forEach
            catchAll {
                onDebug {
                    logger.info("Process command: ${kclass.simpleName}")
                }
                @Suppress("UNCHECKED_CAST")
                val commandHolder = CommandHolder(kclass as KClass<out CommandHandler>)
                val names = commandHolder.name.toMutableSet().apply { remove(commandHolder.name.first()) }
                val command = create(commandHolder.name.first(), plugin)
                command.executor = CommandManager
                command.tabCompleter = CommandManager
                command.aliases = names.toMutableList()
                onDebug {
                    logger.info("Register command: ${kclass.simpleName}")
                }
                value.register(commandHolder.name.first(), command)
                commandHolders += commandHolder
                commandHolder.name.forEach { name ->
                    commandNames.add(name)
                }
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val root = commandHolders.find { holder -> holder.name.any { it.equals(label, true) } } ?: return false
        root.invoke((command as PluginCommand).plugin as Spikot, CommandContext(sender, command, label, args.asList()), 0)
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String> {
        val root = commandHolders.find { holder -> holder.name.any { it.equals(label, true) } }
        return root?.complete(plugin, CommandContext(commandSender, command, label, args.asList()), 0)?.toMutableList()
            ?: commandNames
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
            val handler = commandHandler.createInstance()
            try {
                try {
                    handler.initialize(commandContext, plugin, depth)
                    handler.execute()
                } catch (e: Exception) {
                    handler.onException(e)
                }
            } catch (exception: NoSuchCommandException) {
                usage(commandContext, depth)
            } catch (exception: VerifyException) {
                commandContext.commandSender.sendMessage(exception.message)
            } catch (exception: CastException) {
                commandContext.commandSender.sendMessage(exception.message)
            } catch (e: Exception) {
                catchAll {
                    plugin.onCommandException(commandContext.commandSender, commandContext.command, commandContext.label, commandContext.args.toTypedArray(), e)
                }
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
        val subCommand = child.find { commandContext.args.size > depth && it.name.contains(commandContext.args[depth]) }
        if (subCommand == null) {
            commandContext.commandSender.sendMessage(usage)
            child.forEach {
                if (it.usage.isNotEmpty()) {
                    commandContext.commandSender.sendMessage(it.usage)
                } else {
                    val end = min(commandContext.args.size, depth)
                    if (end <= 0) {
                        commandContext.commandSender.sendMessage("/${commandContext.label} ${it.name.first()}")
                    } else {
                        commandContext.commandSender.sendMessage("/${commandContext.label} ${commandContext.args.subList(0, end).joinToString(" ")} ${it.name.first()}")
                    }
                }
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