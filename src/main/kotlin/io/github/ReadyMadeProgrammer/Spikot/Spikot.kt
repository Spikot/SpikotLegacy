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
        println("Spikot Loading")
        KPlayerListener.start(this)
        initTaskChain(this)
        ModuleManager.load()
        ServerLifeCycle.init(this)
        ModuleManager.addModuleLifeCycle(ServerScope::class,ServerLifeCycle)
    }
    override fun onDisable(){
        ServerLifeCycle.destroy()
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

private object ServerLifeCycle: ModuleLifeCycle<Spikot>{
    private val modules = HashSet<ModuleWrapper<Spikot>>()
    private lateinit var spikot: Spikot
    internal fun init(spikot: Spikot){
        ServerLifeCycle.spikot = spikot
    }
    override fun addModule(annotation: Annotation, module: ModuleWrapper<Spikot>) {
        modules.add(module)
        module.enable(spikot)
    }
    override fun getAllModules(): Set<ModuleWrapper<Spikot>> {
        return modules
    }

    override fun getAllLoadedModule(): Set<ModuleWrapper<Spikot>> {
        return modules
    }
    internal fun destroy(){
        modules.forEach{it.disable()}
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Scope
annotation class ServerScope