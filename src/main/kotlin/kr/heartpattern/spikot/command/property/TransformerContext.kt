package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.ValidationException
import org.bukkit.command.CommandSender

/**
 * Receiver of transformer
 * @param pos Position of property. -1 for command sender, others for argument.
 * @param context Command invocation context
 */
class TransformerContext(private val pos: Int, private val context: CommandContext) {
    /**
     * Plugin which register this command
     */
    val plugin: SpikotPlugin
        get() = context.plugin

    /**
     * Command sender who invoke this command
     */
    val sender: CommandSender
        get() = context.sender

    /**
     * Argument of command
     */
    val args: List<String>
        get() = context.args

    /**
     * Fail transformation and stop executing command
     */
    fun fail(): Nothing {
        throw ValidationException(pos)
    }
}