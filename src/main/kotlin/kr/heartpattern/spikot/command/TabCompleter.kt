@file:Suppress("unused")

package kr.heartpattern.spikot.command

import org.bukkit.command.CommandSender

class TabCompleter(val sender: CommandSender, val args: List<String>, val last: String?)