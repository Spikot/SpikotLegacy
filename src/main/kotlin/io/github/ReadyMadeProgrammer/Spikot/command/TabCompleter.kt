@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.command

import org.bukkit.command.CommandSender

class TabCompleter(val sender: CommandSender, val args: List<String>, val last: String?)