package kr.heartpattern.spikot.command.dsl

import kr.heartpattern.spikot.command.AbstractCommand
import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.internal.CommandNode
import kotlin.reflect.KClass

/**
 * Builder DSL for building child command
 */
class ChildBuilder internal constructor() {
    @PublishedApi
    internal val set: MutableSet<KClass<out AbstractCommand>> = mutableSetOf()

    /**
     * Add child command
     * @param T Type of child command
     */
    inline fun <reified T : AbstractCommand> add() {
        set += T::class
    }

    /**
     * Add child command
     * @param type Type of child command
     */
    fun <T : AbstractCommand> add(type: KClass<T>) {
        set += type
    }
}