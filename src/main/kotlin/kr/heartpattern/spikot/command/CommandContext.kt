package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.SpikotPlugin
import org.bukkit.command.CommandSender

/**
 * Command execution context
 * @param plugin Plugin which register this command
 * @param sender CommandSender who execute this command
 * @param args Argument list of this execution
 */
data class CommandContext(
    val plugin: SpikotPlugin,
    val sender: CommandSender,
    val label: String,
    val args: List<String>
)