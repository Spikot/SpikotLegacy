package kr.heartpattern.spikot.command.internal

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.command.CommandContext
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

internal class SpikotTabExecutor(val root: CommandNode, val plugin: SpikotPlugin) : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<out String>): Boolean {
        root.execute(CommandContext(plugin, sender, args.toList()))
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command?, alias: String?, args: Array<out String>): MutableList<String> {
        return root.complete(CommandContext(plugin, sender, args.toList())).toMutableList()
    }
}