@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.menu

import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

typealias EventHandler = (Point, ClickType) -> Unit

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class MenuDsl

data class Point(val x: Int, val y: Int) {
    constructor(index: Int) : this(index % 9, index / 9)
}

@MenuDsl
class MenuBuilder {
    internal val slot: MutableMap<Point, Slot> = HashMap()
    var title: String = ""
    var size: Int = 54
    fun slot(x: Int, y: Int, build: SlotBuilder.() -> Unit) {
        val builder = SlotBuilder()
        builder.build()
        slot[Point(x, y)] = Slot(builder.item, builder.eventHandlers)
    }

    fun slot(i: Int, build: SlotBuilder.() -> Unit) {
        val builder = SlotBuilder()
        builder.build()
        slot[Point(i % 9, i / 9)] = Slot(builder.item, builder.eventHandlers)
    }
}

@MenuDsl
class SlotBuilder {
    var item: ItemStack = ItemStack(Material.AIR)
    internal val eventHandlers = HashSet<EventHandler>()
    operator fun EventHandler.unaryPlus() {
        eventHandlers += this
    }

    operator fun (() -> Unit).unaryPlus() {
        eventHandlers += { _: Point, _: ClickType -> this() }
    }
}

internal data class Slot(val itemStack: ItemStack, val eventHandler: MutableSet<EventHandler>)