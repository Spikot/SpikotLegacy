package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

abstract class Menu{
    lateinit var menu: MenuBuilder
    protected fun config(build: MenuBuilder.()->Unit){
        menu = MenuBuilder(this::class)
        menu.build()
    }

    protected fun update(build: MenuBuilder.() -> Unit) {
        menu.build()
        menu.update()
    }

    open fun onClose(player: Player, inventory: Inventory): Boolean = true
    open fun onOpen(player: Player, inventory: Inventory) {}
}
