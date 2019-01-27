package io.github.ReadyMadeProgrammer.Spikot.gson

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.ReadyMadeProgrammer.Spikot.logger
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

val gson: Gson
    get() = GsonManager.gson

object GsonManager {
    internal lateinit var gson: Gson
    fun initialize(serializers: Set<KClass<out GsonSerializer<*>>>) {
        val gsonBuilder = Converters.registerAll(GsonBuilder())
                .setPrettyPrinting()
        serializers.forEach {
            try {
                val instance = it.createInstance()
                if (it.findAnnotation<Serializer>()?.hierarchy == true) {
                    gsonBuilder.registerTypeHierarchyAdapter(it.java, instance)
                } else {
                    gsonBuilder.registerTypeAdapter(it.java, instance)
                }
            } catch (e: Exception) {
                logger.warn(e) { "Exception while register type adapter: ${it.simpleName}" }
            }
        }
        gson = gsonBuilder.create()
    }
}