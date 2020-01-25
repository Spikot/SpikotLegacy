package kr.heartpattern.spikot.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.plugin.IllegalPluginAccessException
import kotlin.reflect.KClass

fun KClass<out Event>.getEventListeners(): HandlerList {
    return try {
        val method = getRegistrationClass(java).getDeclaredMethod("getHandlerList")
        method.isAccessible = true
        method.invoke(null) as HandlerList
    } catch (e: Exception) {
        throw IllegalPluginAccessException(e.toString())
    }
}

private fun getRegistrationClass(clazz: Class<out Event>): Class<out Event> {
    return try {
        clazz.getDeclaredMethod("getHandlerList")
        clazz
    } catch (e: NoSuchMethodException) {
        if (clazz.superclass != null && clazz.superclass != Event::class.java
            && Event::class.java.isAssignableFrom(clazz.superclass)) {
            getRegistrationClass(clazz.superclass.asSubclass(Event::class.java))
        } else {
            throw IllegalPluginAccessException("Unable to find handler list for event " + clazz.name + ". Static getHandlerList method required!")
        }
    }
}