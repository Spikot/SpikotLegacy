package kr.heartpattern.spikot.event.hot

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import kotlin.reflect.KClass

object HotEventManager {
    private val registry = HashMap<Class<out Event>, HotEventExecutorGroup<*>>()

    private fun <E : Event> getExecutorGroup(event: KClass<E>): HotEventExecutorGroup<E> {
        val registered = registry[event.java]
        if (registered != null) return registered as HotEventExecutorGroup<E>

        synchronized(this) {
            val doubleCheck = registry[event.java]
            if (doubleCheck != null) return doubleCheck as HotEventExecutorGroup<E>

            val new = HotEventExecutorGroup(event)
            registry[event.java] = new
            return new
        }
    }

    fun <E : Event> register(
        event: KClass<E>,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        listener: (E, HotEventListener<E>) -> Unit
    ): HotEventListener<E> {
        val executorGroup = getExecutorGroup(event)
        return executorGroup.register(priority, ignoreCancelled, listener)
    }

    inline fun <reified E : Event> register(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        noinline listener: (E, HotEventListener<E>) -> Unit
    ): HotEventListener<E> {
        return register(E::class, priority, ignoreCancelled, listener)
    }
}