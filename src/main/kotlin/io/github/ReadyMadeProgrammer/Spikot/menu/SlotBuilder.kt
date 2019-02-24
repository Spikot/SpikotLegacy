package io.github.ReadyMadeProgrammer.Spikot.menu

import io.github.ReadyMadeProgrammer.Spikot.item.DefaultItemBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class ClickEvent(val point: Point, val type: ClickType)

@MenuDsl
class SlotBuilder {
    var item: ItemStack = ItemStack(Material.AIR)
    internal val eventHandlers = HashSet<ClickHandler>()

    fun handler(handler: ClickEvent.() -> Unit) {
        eventHandlers += { point: Point, type: ClickType -> ClickEvent(point, type).handler() }
    }

    fun item(material: Material, itemBuilder: DefaultItemBuilder.() -> Unit) {
        item = io.github.ReadyMadeProgrammer.Spikot.item.item(material, itemBuilder)
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(ClickHandler)"))
    operator fun ClickHandler.unaryPlus() {
        eventHandlers += this
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(()->Unit)"))
    operator fun (() -> Unit).unaryPlus() {
        eventHandlers += { _: Point, _: ClickType -> this() }
    }
}

internal data class Slot(val itemStack: ItemStack, val clickHandler: MutableSet<ClickHandler>)