package io.github.ReadyMadeProgrammer.Spikot.config

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@PublishedApi
internal class ListConfigProperty<T : Any>(
        private val type: KClass<T>,
        private val name: String? = null
) : ReadWriteProperty<ConfigSpec, MutableList<T>> {
    private var cache: MutableList<T> = mutableListOf()
    private var cached: Boolean = false
    override fun getValue(thisRef: ConfigSpec, property: KProperty<*>): MutableList<T> {
        if (!cached) {
            val value = thisRef.yaml.getListByType("${thisRef.path}.${name ?: property.name}", type) as MutableList<T>?
            if(value==null){
                cache = mutableListOf()
                thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", cache)
            }
            else{
                cache = value
            }
            cached = true
        }
        return cache
    }

    override fun setValue(thisRef: ConfigSpec, property: KProperty<*>, value: MutableList<T>) {
        cache = value
        cached = true
        thisRef.yaml.set("${thisRef.path}.${name ?: property.name}", value)
    }
}