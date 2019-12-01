package kr.heartpattern.spikot.gson

import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import kr.heartpattern.spikot.plugin.FindAnnotation
import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Inherited
@FindAnnotation(impl = [GsonSerializer::class])
annotation class Serializer(
    val hierarchy: Boolean = true
)

interface GsonSerializer<T> : JsonSerializer<T>, JsonDeserializer<T>