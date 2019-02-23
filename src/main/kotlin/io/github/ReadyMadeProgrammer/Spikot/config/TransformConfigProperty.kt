package io.github.ReadyMadeProgrammer.Spikot.config

import io.github.ReadyMadeProgrammer.Spikot.misc.Converter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@PublishedApi
internal class TransformConfigProperty<T : Any, R : Any>(private val rawType: KClass<T>, private val default: R, private val converter: Converter<R, T>) : ReadWriteProperty<ConfigSpec, R> {
    private var cache: R? = null
    private var cached = false
    override fun getValue(thisRef: ConfigSpec, property: KProperty<*>): R {
        if (!cached) {
            val raw = thisRef.yaml.getByType("${thisRef.path}.${property.name}", rawType)
            if (raw != null) {
                cache = converter.read(raw as T)
            }
            cached = true
        }
        return cache ?: default
    }

    override fun setValue(thisRef: ConfigSpec, property: KProperty<*>, value: R) {
        thisRef.yaml.set(property.name, converter.write(value))
        cache = value
        cached = true
    }
}