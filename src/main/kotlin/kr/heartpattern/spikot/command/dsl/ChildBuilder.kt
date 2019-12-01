package kr.heartpattern.spikot.command.dsl

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.internal.CommandNode
import kotlin.reflect.KClass

class ChildBuilder internal constructor() {
    @PublishedApi
    internal val set: MutableSet<KClass<out AbstractCommand>> = mutableSetOf()

    inline fun <reified T : AbstractCommand> add() {
        set += T::class
    }

    fun <T : AbstractCommand> add(type: KClass<T>) {
        set += type
    }
}