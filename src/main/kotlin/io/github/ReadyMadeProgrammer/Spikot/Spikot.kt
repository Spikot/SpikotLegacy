package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.KommandFramework.KommandException
import io.github.ReadyMadeProgrammer.Spikot.command.SpigotCommandExecutor
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * Entry point of plugin which use Spikot Framework.
 * Plugin lifecycle is managed by spikot so developer should not override any of method in JavaPlugin
 * Developer should define their plugin main class like this.
 * class PluginMain: Spikot()
 */
open class Spikot: JavaPlugin(){
    private lateinit var logger: KLogger

    final override fun onEnable() {
        logger = KotlinLogging.logger(description.name)
    }

    final override fun onDisable() {
    }

    final override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String>?): Boolean {
        try {
            SpigotCommandExecutor.onCommand(label!!, args!!, sender!!)
        } catch(e: KommandException){
            //Ignore
        } catch(e: Exception){
            e.printStackTrace()
        }
        return true
    }

    final override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<String>?): MutableList<String> {
        try {
            return SpigotCommandExecutor.onComplete(alias!!, args!!, sender!!).toMutableList()
        } catch(e: KommandException){
            //Ignore
        } catch(e: Exception){
            e.printStackTrace()
        }
        return mutableListOf()
    }
}