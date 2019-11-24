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

class SuspendCommandProperty<T>(val pos: Int, val value: suspend CommandContext.() -> T) : SuspendTransformableProperty<T> {
    private var cache: Option<T> = None
    private val suspendValidators: MutableList<suspend TransformerContext.(T) -> Boolean> = LinkedList()
    private val validators: MutableList<TransformerContext.(T) -> Boolean> = LinkedList()
    private val childs: MutableList<SuspendCommandProperty<*>> = LinkedList()

    internal suspend fun initialize(context: CommandContext) {
        if (cache == None) {
            val fetch = context.value()
            val transformerContext = TransformerContext(pos, context)
            for (validator in validators)
                if (!transformerContext.validator(fetch))
                    throw ValidationException(pos)

            for (validator in suspendValidators)
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
        return (cache as? Just<T>)?.value ?: throw PropertyNotInitializedException(property)
    }

    override fun <R> transform(transformer: CommandContext.(T) -> R): SuspendCommandProperty<R> {
        val child = SuspendCommandProperty<R>(pos) { transformer((cache as Just<T>).value) }
        childs += child
        return child
    }

    override fun validate(validator: TransformerContext.(T) -> Boolean): SuspendCommandProperty<T> {
        validators += validator
        return this
    }

    override fun <R> suspendTransform(transformer: suspend CommandContext.(T) -> R): SuspendCommandProperty<R> {
        val child = SuspendCommandProperty<R>(pos) { transformer((cache as Just<T>).value) }
        childs += child
        return child
    }

    override fun suspendValidate(validator: suspend TransformerContext.(T) -> Boolean): SuspendCommandProperty<T> {
        suspendValidators += validator
        return this
    }
}