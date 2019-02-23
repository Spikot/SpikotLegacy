package io.github.ReadyMadeProgrammer.Spikot.persistence.gson

import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Inherited
annotation class Serializer(
        val hierarchy: Boolean = true
)

@Serializer
interface GsonSerializer<T> : JsonSerializer<T>, JsonDeserializer<T>, InstanceCreator<T>