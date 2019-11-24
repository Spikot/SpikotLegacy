package kr.heartpattern.spikot.command.property

import kr.heartpattern.spikot.command.Command
import kr.heartpattern.spikot.command.CommandContext
import kr.heartpattern.spikot.command.PropertyNotInitializedException
import kr.heartpattern.spikot.command.ValidationException
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import java.util.*
import kotlin.reflect.KProperty

class CommandProperty<T>(val pos: Int, val value: CommandContext.() -> T) : TransformableProperty<T> {
    private var cache: Option<T> = None
    private val validators: MutableList<TransformerContext.(T) -> Boolean> = LinkedList()
    private val childs: MutableList<CommandProperty<*>> = LinkedList()

    internal fun initialize(context: CommandContext) {
        if (cache == None) {
            val fetch = context.value()
            val transformerContext = TransformerContext(pos, context)
            for (validator in validators)
                if (!transformerContext.validator(fetch))
                    throw ValidationException(pos)
            cache = Just(fetch)
        }
        for (child in childs) {
            child.initialize(context)
        }
    }

    override val isInitialized: Boolean
        get() = cache is Just<T>

    override fun getValue(thisRef: Command, property: KProperty<*>): T {
        return (cache as? Just<T>)?.value?: throw PropertyNotInitializedException(property)
    }

    override fun <R> transform(transformer: CommandContext.(T) -> R): CommandProperty<R> {
        val child = CommandProperty<R>(pos) { transformer((cache as Just<T>).value) }
        childs += child
        return child
    }

    override fun validate(validator: TransformerContext.(T) -> Boolean): CommandProperty<T> {
        validators += validator
        return this
    }
}