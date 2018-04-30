package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import io.github.ReadyMadeProgrammer.Spikot.attachInvisible
import io.github.ReadyMadeProgrammer.Spikot.itemBuilder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

typealias MenuEvent = (Menu, Player, Slot, ClickType) -> Unit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class MenuDslMarker

class Coordinate(val x: Int, val y: Int)

data class Slot(val item: ItemStack, internal val events: MutableSet<MenuEvent>)

@MenuDslMarker
class MenuBuilder(internal val menuClass: KClass<out Menu>) {
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
        slots[Coordinate(x, y)] = slotBuilder.toSlot(Coordinate(x, y))
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

@MenuDslMarker
class SlotBuilder{
    var item: ItemBuilder? = null
    val events = mutableSetOf<MenuEvent>()
    fun events(build: Inputs<MenuEvent>.()->Unit){
        val builder = Inputs<MenuEvent>()
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