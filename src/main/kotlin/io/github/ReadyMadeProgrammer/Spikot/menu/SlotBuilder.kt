package io.github.ReadyMadeProgrammer.Spikot.menu

import io.github.ReadyMadeProgrammer.Spikot.item.DefaultItemBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

@MenuDsl
class SlotBuilder {
    var item: ItemStack = ItemStack(Material.AIR)
    internal val eventHandlers = HashSet<EventHandler>()

    fun handle(handler: EventHandler) {
        eventHandlers += handler
    }

    fun handle(handler: () -> Unit) {
        eventHandlers += { _: Point, _: ClickType -> handler() }
    }

    fun item(material: Material, itemBuilder: DefaultItemBuilder.() -> Unit) {
        item = io.github.ReadyMadeProgrammer.Spikot.item.item(material, itemBuilder)
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(EventHandler)"))
    operator fun EventHandler.unaryPlus() {
        eventHandlers += this
    }

    @Deprecated("Too complex structure", ReplaceWith("handler(()->Unit)"))
    operator fun (() -> Unit).unaryPlus() {
        eventHandlers += { _: Point, _: ClickType -> this() }
    }
}

internal data class Slot(val itemStack: ItemStack, val eventHandler: MutableSet<EventHandler>)