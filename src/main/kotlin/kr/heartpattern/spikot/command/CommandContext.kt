package kr.heartpattern.spikot.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

data class CommandContext(val commandSender: CommandSender, val command: Command, val label: String, val args: List<String>)