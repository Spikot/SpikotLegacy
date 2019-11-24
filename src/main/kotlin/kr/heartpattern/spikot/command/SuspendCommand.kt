package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.command.property.SuspendCommandProperty
import org.bukkit.command.CommandSender
import java.util.*

abstract class SuspendCommand : AbstractCommand() {
    internal val suspendProperties = LinkedList<SuspendCommandProperty<*>>()
    abstract suspend fun execute()

    fun suspendSender(): SuspendCommandProperty<CommandSender> {
        val property = SuspendCommandProperty(-1) { context.sender }
        suspendProperties += property
        return property
    }

    fun suspendArg(position: Int): SuspendCommandProperty<String?> {
        val property = SuspendCommandProperty(position) {
            if (context.args.size > position)
                context.args[position]
            else
                null
        }
        suspendProperties += property
        return property
    }

    fun suspendArgs(range: IntRange): SuspendCommandProperty<List<String>> {
        val property = SuspendCommandProperty(range.first) {
            when {
                context.args.size > range.last -> context.args.subList(range.first, range.last + 1)
                context.args.size > range.first -> context.args.subList(range.first, context.args.size)
                else -> emptyList()
            }
        }
        suspendProperties += property
        return property
    }

    fun suspendRemains(start: Int): SuspendCommandProperty<List<String>> = suspendArgs(start..Int.MAX_VALUE)
}