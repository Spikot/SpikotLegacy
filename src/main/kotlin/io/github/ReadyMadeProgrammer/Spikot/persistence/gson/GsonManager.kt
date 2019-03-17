package io.github.ReadyMadeProgrammer.Spikot.persistence.gson

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.ReadyMadeProgrammer.Spikot.module.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

val gson: Gson
    get() = GsonManager.gson

@Module(LoadOrder.API - 1000)
object GsonManager : AbstractModule() {
    internal lateinit var gson: Gson
    override fun onEnable() {
        val gsonBuilder = Converters.registerAll(GsonBuilder())
                .setPrettyPrinting()
        SpikotPluginManager.spikotPlugins.asSequence().flatMap { it.serializer.asSequence() }.filter { it.canLoad() }.forEach {
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