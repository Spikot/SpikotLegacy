package kr.heartpattern.spikot.command.internal.handler

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.SuspendCommand
import kr.heartpattern.spikot.command.dsl.Description
import kr.heartpattern.spikot.command.internal.CommandNode
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.isSubclassOf

abstract class AbstractCommandHandler(val type: KClass<out AbstractCommand>) {
    companion object {
        fun create(type: KClass<out AbstractCommand>): AbstractCommandHandler {
            return if (type.isSubclassOf(SuspendCommand::class)) {
                SuspendCommandHandler(type)
            } else {
                CommandHandler(type)
            }
        }
    }

    private val companion = type.companionObjectInstance as Description

    open val names: Collection<String> = companion.name

    internal open val childs: Collection<CommandNode> = companion.childClasses.map{CommandNode(it)}

    private val childNames
        get() = childs.asSequence().flatMap { it.handler.names.asSequence() }

    open fun complete(context: CommandContext): List<String> {
        return completeChild(context) + companion.completer(context)
    }

    open fun completeChild(context: CommandContext): List<String> {
        val last = context.args.lastOrNull() ?: ""
        return childNames.filter { it.startsWith(last) }.toMutableList()
    }

    open fun help(): String {
        return companion.help
    }

    open fun usage(): String {
        return companion.usage
    }

    abstract fun execute(context: CommandContext)
}