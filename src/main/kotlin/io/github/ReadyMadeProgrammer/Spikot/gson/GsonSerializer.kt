package io.github.ReadyMadeProgrammer.Spikot.gson

import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Inherited
annotation class GsonSerializerAnnotation

@GsonSerializerAnnotation
interface GsonSerializer<T> : JsonSerializer<T>, JsonDeserializer<T>, InstanceCreator<T>