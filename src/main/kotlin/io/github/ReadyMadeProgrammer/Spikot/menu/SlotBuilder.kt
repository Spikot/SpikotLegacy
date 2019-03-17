package io.github.ReadyMadeProgrammer.Spikot.menu

import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class ClickEvent(val point: SlotPosition, val type: ClickType)

@MenuDsl
class SlotBuilder(val slot: SlotPosition) {
    var item: ItemStack = ItemStack(Material.AIR)
    internal val eventHandlers = HashSet<ClickHandler>()

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