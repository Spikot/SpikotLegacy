package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import org.bukkit.command.CommandSender


class TransformerContext(private val pos: Int, private val context: CommandContext) {
    val plugin: SpikotPlugin
        get() = context.plugin

    val sender: CommandSender
        get() = context.sender

    val args: List<String>
        get() = context.args

    fun fail(): Nothing {
        throw ValidationException(pos)
    }
}