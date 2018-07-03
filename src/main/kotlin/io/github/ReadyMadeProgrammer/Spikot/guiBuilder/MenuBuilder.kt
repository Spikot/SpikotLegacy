package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import io.github.ReadyMadeProgrammer.Spikot.attachInvisible
import io.github.ReadyMadeProgrammer.Spikot.itemBuilder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

typealias MenuReciever = (Menu, MenuEvent) -> Boolean

data class MenuEvent(val player: Player, val slot: Slot, val clickType: ClickType)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class MenuDslMarker

class Coordinate(val x: Int, val y: Int)

data class Slot(val item: ItemStack, internal val events: MutableSet<MenuReciever>)

@MenuDslMarker
class MenuBuilder(internal val menuClass: Menu) {
    var title = ""
    var size = 54
    internal var isClosable = false
    val Closeable: Unit
        get(){
            isClosable = true
        }
    internal val slots = mutableMapOf<Coordinate, Slot>()
    internal operator fun get(c: Int): Slot? = slots[Coordinate(c % 9, c / 9)]
    internal operator fun get(x: Int, y: Int): Slot? = slots[Coordinate(x, y)]
    fun slot(x: Int, y: Int, build: SlotBuilder.()->Unit){
        if ((size < 9 && x >= size) || x < 0 || x >= 9 || y < 0 || y >= size / 9) throw IndexOutOfBoundsException("Slot location out of bound")
        val slotBuilder = SlotBuilder()
        slotBuilder.build()
        slot(x, y, slotBuilder)
    }

    fun slot(x: Int, y: Int, slot: SlotBuilder) {
        slots[Coordinate(x, y)] = slot.toSlot(Coordinate(x, y))
    }

    internal val inventory: Inventory
        get() {
            val inventory = Bukkit.createInventory(null, size, title)
            slots.forEach { coordinate, slot ->
                inventory.setItem(coordinate.x + coordinate.y * 9, slot.item)
            }
            return inventory
        }

    val update: () -> Unit = {}
}

fun slot(build: SlotBuilder.() -> Unit): SlotBuilder {
    val builder = SlotBuilder()
    builder.build()
    return builder
}

@MenuDslMarker
class SlotBuilder{
    var item: ItemBuilder? = null
    val events = mutableSetOf<MenuReciever>()
    fun events(build: Inputs<MenuReciever>.() -> Unit) {
        val builder = Inputs<MenuReciever>()
        builder.build()
        events.addAll(builder.set)
    }

    fun toSlot(loc: Coordinate) = Slot(transformItem(loc), events)

    fun transformItem(loc: Coordinate): ItemStack {
        if (item == null) throw NullPointerException("Item is null")
        val cloned = item!!.toItemStack().clone()
        cloned.itemMeta.displayName = cloned.itemMeta.displayName.attachInvisible(loc.x + loc.y * 9)
        return cloned
    }
}
@MenuDslMarker
class Inputs<T>{
    internal val set = mutableSetOf<T>()
    operator fun T.unaryPlus(){
        set.add(this)
    }
}

enum class MenuEventType{
    RIGHT,LEFT,SHIFT_RIGHT,SHIFT_LEFT
}