/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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