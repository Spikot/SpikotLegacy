package io.github.ReadyMadeProgrammer.Spikot.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.ReadyMadeProgrammer.Spikot.logger
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

val gson: Gson
    get() = GsonManager.gson

object GsonManager {
    internal lateinit var gson: Gson
    fun initialize(serializers: Set<KClass<out GsonSerializer<*>>>) {
        val gsonBuilder = GsonBuilder().setPrettyPrinting()
        serializers.forEach {
            try {
                val instance = it.createInstance()
                gsonBuilder.registerTypeHierarchyAdapter(it.java, instance)
            } catch (e: Exception) {
                logger.warn(e) { "Exception while register type adapter: ${it.simpleName}" }
            }
        }
        gson = gsonBuilder.create()
    }
}