package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.Spikot.command.CastException
import io.github.ReadyMadeProgrammer.Spikot.command.CommandManager
import io.github.ReadyMadeProgrammer.Spikot.command.VerifyException
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * Entry point of plugin which use Spikot Framework.
 * Plugin lifecycle is managed by spikot so developer should not override any of method in JavaPlugin
 * Developer should define their plugin main class like this.
 * class PluginMain: Spikot()
 */
abstract class Spikot : JavaPlugin() {
    private lateinit var logger: KLogger

    final override fun onEnable() {
        logger = KotlinLogging.logger(description.name)
    }

    final override fun onDisable() {
    }

    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        try {
            CommandManager.invoke(sender, command, label, args)
        } catch (exception: Exception) {
            onCommandException(sender, command, label, args, exception)
        }
        return true
    }

    open fun onCommandException(sender: CommandSender, command: Command, label: String, args: Array<String>, exception: Exception) = when (exception) {
        is CastException -> sender.sendMessage(exception.message)
        is VerifyException -> sender.sendMessage(exception.message)
        else -> {
            sender.sendMessage(exception.message)
            logger.warn(exception) { "Exception occur while running command \"$label\"" }
        }
    }

    final override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {
        try {
            return CommandManager.complete(sender, command, label, args).toMutableList()
        } catch (exception: Exception) {
            onCommandException(sender, command, label, args, exception)
        }
        return Collections.emptyList()
    }
}