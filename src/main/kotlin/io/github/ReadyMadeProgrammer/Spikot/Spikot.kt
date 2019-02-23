@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.command.CommandManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

/**
 * Entry point of plugin which use Spikot Framework.
 * Plugin lifecycle is managed by spikot so developer should not override any of method in JavaPlugin
 * Developer should define their plugin main class like this.
 * class PluginMain: Spikot()
 */
abstract class Spikot : JavaPlugin() {
    companion object {
        fun getPlugin(name: String): Plugin {
            return Bukkit.getPluginManager().getPlugin(name)
        }

        inline fun <reified T> getPlugin(): T {
            return Bukkit.getPluginManager().plugins.find { it is T } as T
        }
    }

    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return CommandManager.onCommand(sender, command, label, args)
    }

    final override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {
        return CommandManager.onTabComplete(sender, command, label, args)
    }
}