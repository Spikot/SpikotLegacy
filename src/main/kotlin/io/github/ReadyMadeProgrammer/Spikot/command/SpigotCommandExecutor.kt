package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.KommandFramework.CommandExecutable
import io.github.ReadyMadeProgrammer.KommandFramework.CommandExecutor
import org.bukkit.command.CommandSender

object SpigotCommandExecutor{
    private val commandExecutor = CommandExecutor<CommandSender>()
    fun addCommand(commandExecutable: CommandExecutable<CommandSender>)
        =commandExecutor.addCommand(commandExecutable)

    fun onCommand(command: String,arguments: Array<String>,sender: CommandSender)
        =commandExecutor.execute(command,arguments.toList(),sender)

    fun onComplete(command: String, arguments: Array<String>, sender: CommandSender)
        =commandExecutor.complete(command,arguments.toList(),sender)
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Disable