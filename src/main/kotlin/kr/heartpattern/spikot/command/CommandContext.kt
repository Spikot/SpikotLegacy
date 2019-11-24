package kr.heartpattern.spikot.command

import kr.heartpattern.spikot.SpikotPlugin
import org.bukkit.command.CommandSender

data class CommandContext(
    val plugin: SpikotPlugin,
    val sender: CommandSender,
    val args: List<String>
)