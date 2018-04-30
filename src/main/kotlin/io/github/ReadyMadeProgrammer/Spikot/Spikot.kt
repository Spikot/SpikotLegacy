package io.github.ReadyMadeProgrammer.Spikot

import io.github.ReadyMadeProgrammer.KommandFramework.KommandException
import io.github.ReadyMadeProgrammer.Spikot.command.SpigotCommandExecutor
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

open class Spikot: JavaPlugin(){
    lateinit var logger: KLogger
    override fun onEnable(){
        logger = KotlinLogging.logger(description.name)
        ServerLifeCycle.registerPlugin(this)
    }
    override fun onDisable(){
    }

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String>?): Boolean {
        try {
            SpigotCommandExecutor.onCommand(label!!, args!!, sender!!)
        }
        catch(e: KommandException){
            //Ignore
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<String>?): MutableList<String> {
        try {
            return SpigotCommandExecutor.onComplete(alias!!, args!!, sender!!).toMutableList()
        }
        catch(e: KommandException){
            //Ignore
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        return mutableListOf()
    }
}

internal object ServerLifeCycle : ModuleLifeCycle<Spikot, ServerScope> {
    private val modules = HashMap<String, HashSet<ModuleWrapper<Spikot>>>()
    private val plugins = HashMap<String, Spikot>()
    internal fun registerPlugin(spikot: Spikot) {
        plugins[spikot.description.name] = spikot
    }

    override fun addModule(annotation: ServerScope, module: ModuleWrapper<Spikot>) {
        val sets = modules[annotation.value]
        val plugin = plugins[annotation.value]
        if (sets == null || plugin == null) {
            logger.warn("Invalid plugin name for module(${module.module.simpleName}): ${(annotation.value)}")
            return
        }
        sets.add(module)
        module.enable(plugin)
    }
    override fun getAllModules(): Set<ModuleWrapper<Spikot>> {
        val sets = mutableSetOf<ModuleWrapper<Spikot>>()
        modules.forEach { _, v -> sets.addAll(v) }
        return sets
    }

    override fun getAllLoadedModule(): Set<ModuleWrapper<Spikot>> {
        return getAllModules()
    }
    internal fun destroy(){
        modules.flatMap { (_, v) -> v }.forEach { it.disable() }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Scope
annotation class ServerScope(val value: String)