package io.github.ReadyMadeProgrammer.Spikot.menu

import io.github.ReadyMadeProgrammer.Spikot.thread.sync
import io.github.ReadyMadeProgrammer.Spikot.utils.attachInvisible
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.properties.Delegates

abstract class MenuProvider {
    var id by Delegates.notNull<Int>()
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var player: Player
    internal lateinit var menu: MenuBuilder
    protected lateinit var inventory: Inventory
    private var opened = false
    @Suppress("unused")
    private val isOpen
        get() = opened

    private fun updateInventory() {
        menu.slot.forEach { (point, item) ->
            player.openInventory.topInventory.setItem(point.x + point.y * 9, item.itemStack)
        }
        player.updateInventory()
    }

    internal fun openInventory() {
        inventory = Bukkit.createInventory(null, menu.size, menu.title.attachInvisible(id))
        val contents = Array(menu.size) { ItemStack(Material.AIR) }
        menu.slot.forEach { (point, item) ->
            contents[point.x + point.y * 9] = item.itemStack
        }
        inventory.contents = contents
        opened = true
        player.safeOpenInventory(inventory)
    }

    internal fun initialize(id: Int, player: Player, menu: MenuBuilder) {
        this.id = id
        this.player = player
        this.menu = menu
    }

    @Suppress("unused")
    protected fun update(build: MenuBuilder.() -> Unit) {
        menu.build()
        if (opened) {
            sync {
                updateInventory()
            }
        }
    }

    abstract fun onOpen()

    open fun onClose(): Boolean {
        return true
    }

    open fun onInteract(event: InventoryInteractEvent) {
        event.isCancelled = true
    }
}