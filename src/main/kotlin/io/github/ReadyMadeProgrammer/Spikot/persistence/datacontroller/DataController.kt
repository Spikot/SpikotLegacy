package io.github.ReadyMadeProgrammer.Spikot.persistence.datacontroller

import java.io.File
import kotlin.reflect.KClass

interface DataController<K : Any, V : Any> {
    fun initialize(directory: File, valueType: KClass<*>)
    fun destroy()
}