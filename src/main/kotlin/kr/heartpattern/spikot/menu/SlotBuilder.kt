package kr.heartpattern.spikot.menu

import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

/**
 * Represent click information
 * @param point Position information about where is clicked
 * @param type Type of click
 */
class ClickEvent(val point: SlotPosition, val type: ClickType)

/**
 * Configure slot
 */
@MenuDsl
class SlotBuilder(val slot: SlotPosition) {
    /**
     * Item that display in slot
     */
    var item: ItemStack = ItemStack(Material.AIR)
    internal val eventHandlers = HashSet<ClickHandler>()

    /**
     * Add handler which invoked when player click this slot
     * @param handler Handler to receive click event
     */
    fun handler(handler: ClickEvent.() -> Unit) {
        eventHandlers += { point: SlotPosition, type: ClickType -> ClickEvent(point, type).handler() }
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(ClickHandler)"))
    operator fun ClickHandler.unaryPlus() {
        eventHandlers += this
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(()->Unit)"))
    operator fun (() -> Unit).unaryPlus() {
        eventHandlers += { _: SlotPosition, _: ClickType -> this() }
    }
}

internal data class Slot(val itemStack: ItemStack, val clickHandler: MutableSet<ClickHandler>)