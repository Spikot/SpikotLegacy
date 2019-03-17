@file:Suppress("unused")

package io.github.ReadyMadeProgrammer.Spikot.menu

import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import io.github.ReadyMadeProgrammer.Spikot.item.item as itemBuilderMethod

typealias ClickHandler = (SlotPosition, ClickType) -> Unit

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class MenuDsl

data class SlotPosition(val x: Int, val y: Int) {
    constructor(index: Int) : this(index % 9, index / 9)

    val index: Int
        get() = y * 9 + x

    fun getSlot(inventory: Inventory): ItemStack = inventory.getItem(index)
}

@MenuDsl
class MenuBuilder {
    internal val slot: MutableMap<SlotPosition, Slot> = HashMap()
    var title: String = ""
    var size: Int = 54
    fun slot(x: Int, y: Int, build: SlotBuilder.() -> Unit) {
        slot(SlotPosition(x, y), build)
    }

    fun slot(x: Int, y: Iterable<Int>, build: SlotBuilder.() -> Unit) {
        y.forEach { slot(x, it, build) }
    }

    fun slot(x: Iterable<Int>, y: Int, build: SlotBuilder.() -> Unit) {
        x.forEach { slot(it, y, build) }
    }

    fun slot(x: Iterable<Int>, y: Iterable<Int>, build: SlotBuilder.() -> Unit) {
        x.forEach { xe -> y.forEach { ye -> slot(xe, ye, build) } }
    }

    fun slot(i: Int, build: SlotBuilder.() -> Unit) {
        slot(i % 9, i / 9, build)
    }

    fun slot(i: Iterable<Int>, build: SlotBuilder.() -> Unit) {
        i.forEach { slot(it, build) }
    }

    fun slot(point: SlotPosition, build: SlotBuilder.() -> Unit) {
        val builder = SlotBuilder(point)
        builder.build()
        slot[point] = Slot(builder.item, builder.eventHandlers)
    }
}