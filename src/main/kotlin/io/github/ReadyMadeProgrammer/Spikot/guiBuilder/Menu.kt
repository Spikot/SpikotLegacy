package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import org.bukkit.entity.Player

abstract class Menu{
    internal val builder = MenuBuilder()
    abstract fun init()
    protected fun config(build: MenuBuilder.()->Unit){
        builder.build()
    }
}
