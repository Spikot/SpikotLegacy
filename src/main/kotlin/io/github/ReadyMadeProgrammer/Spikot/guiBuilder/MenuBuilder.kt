package io.github.ReadyMadeProgrammer.Spikot.guiBuilder

import io.github.ReadyMadeProgrammer.Spikot.itemBuilder.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

typealias MenuEvent = (Player,Slot,MenuEventType)->Unit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@DslMarker
annotation class MenuDslMarker

class Coordinate(val x: Int, val y: Int)

data class Slot(val item: ItemStack, val inventory: Inventory, internal val events: MutableSet<MenuEvent>)

@MenuDslMarker
class MenuBuilder{
    var title: String = ""
    var size: Int = 54
    internal var isClosable = false
    val Closeable: Unit
        get(){
            isClosable = true
        }
    private val slots = mutableMapOf<Coordinate,Slot>()
    fun slot(x: Int, y: Int, build: SlotBuilder.()->Unit){
        val slotBuilder = SlotBuilder()
        slotBuilder.build()
        slots[Coordinate(x,y)] = slotBuilder.toSlot()
    }
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
    fun toSlot(): Slot{
        TODO("not implemented")
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