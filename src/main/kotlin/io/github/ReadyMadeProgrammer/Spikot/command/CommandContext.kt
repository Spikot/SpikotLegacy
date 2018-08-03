package io.github.ReadyMadeProgrammer.Spikot.command

import com.google.common.collect.Multimap
import org.bukkit.command.CommandSender
import java.util.*

data class CommandContext(val flag: Multimap<String, String>, val argument: LinkedList<String>, val sender: CommandSender)