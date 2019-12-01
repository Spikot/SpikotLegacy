@file:Suppress("unused")

package kr.heartpattern.spikot.menu

import kr.heartpattern.spikot.SpikotPlugin
import kr.heartpattern.spikot.misc.MutableFlagProperty
import kr.heartpattern.spikot.misc.MutableProperty
import kr.heartpattern.spikot.module.*
import kr.heartpattern.spikot.spikot
import kr.heartpattern.spikot.thread.runNextSync
import kr.heartpattern.spikot.utils.attachInvisible
import kr.heartpattern.spikot.utils.findInvisible
import kr.heartpattern.spikot.utils.hasInvisible
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal object MenuIdProperty : MutableProperty<Int>
internal object MenuPlayerProperty : MutableProperty<Player>
internal object MenuBuilderProperty : MutableProperty<MenuBuilder>
internal object MenuInventoryProperty : MutableProperty<Inventory>
internal object MenuOpenProperty : MutableFlagProperty

fun Player.safeOpenInventory(inventory: Inventory) {
    spikot.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.openInventory(inventory)
    }
}

fun Player.safeCloseInventory() {
    spikot.runNextSync {
        if (!player.isOnline) return@runNextSync
        this.closeInventory()
    }
}

fun Player.openInventory(plugin: SpikotPlugin, menuProvider: MenuProvider) {
    val handler = ModuleManager.createModule(menuProvider, plugin)
    val builder = MenuBuilder()
    menuProvider.context = handler.context
    handler.context[MenuIdProperty] = MenuManager.id
    handler.context[MenuPlayerProperty] = this
    handler.context[MenuBuilderProperty] = builder
    handler.context[MenuOpenProperty] = false
    MenuManager.openedInventory[menuProvider.id] = handler
    handler.load()
    handler.enable()

    val inventory = Bukkit.createInventory(null, builder.size, builder.title.attachInvisible(menuProvider.id))
    handler.context[MenuInventoryProperty] = inventory
    val contents = Array(builder.size) { ItemStack(Material.AIR) }
    builder.slot.forEach { (point, item) ->
        contents[point.x + point.y * 9] = item.itemStack
    }
    inventory.contents = contents
    handler.context[MenuOpenProperty] = true
    player.safeOpenInventory(inventory)
}

val Inventory.menuProvider: MenuProvider?
    get() {
        return if (title.hasInvisible()) {
            MenuManager.openedInventory[title.findInvisible()]?.module as MenuProvider?
        } else {
            null
        }
    }

@Module @LoadBefore([IModule::class])
@PublishedApi
internal object MenuManager : AbstractModule() {
    internal var id: Int = 0
        get() {
            field++
            return field
        }
        private set

    @PublishedApi
    internal val openedInventory = mutableMapOf<Int, ModuleHandler>()

    @EventHandler
    fun onSlotClick(event: InventoryClickEvent) {
        val topInventory = event.whoClicked.openInventory.topInventory ?: return
        if (topInventory.title == null || !topInventory.title.hasInvisible()) {
            return
        }
        val inventoryId = topInventory.title.findInvisible()
        val provider = openedInventory[inventoryId]?.module as MenuProvider? ?: return
        provider.onInteract(event)
        if (event.clickedInventory === topInventory) {
            val slotId = event.slot
            val x = slotId % 9
            val y = slotId / 9
            val point = SlotPosition(x, y)
            val slot = provider.menu.slot[point] ?: return
            slot.clickHandler.forEach {
                it(point, event.click)
            }
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        val targetInventory = event.whoClicked.openInventory.topInventory
        if (targetInventory.title.hasInvisible()) {
            val inventoryId = targetInventory.title.findInvisible()
            val provider = openedInventory[inventoryId]?.module as MenuProvider? ?: return
            provider.onInteract(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (!event.inventory.title.hasInvisible()) {
            return
        }
        val inventoryId = event.inventory.title.findInvisible()
        val handler = openedInventory[inventoryId] ?: return
        val provider = handler.module as MenuProvider
        if (!provider.onClose()) {
            (event.player as Player).safeOpenInventory(provider.inventory)
        } else {
            openedInventory.remove(inventoryId)
            handler.context[MenuOpenProperty] = false
            handler.disable()
        }
    }
}

inline fun <reified T : MenuProvider> Player.getOpenedInventory(): T? {
    val title = player.openInventory.topInventory?.title
    if (title.isNullOrBlank() || !title.hasInvisible()) return null
    return MenuManager.openedInventory[title.findInvisible()] as? T
}