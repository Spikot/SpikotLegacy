/*
 * Copyright 2020 HeartPattern
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

package kr.heartpattern.spikot.config

import kr.heartpattern.spikot.plugin.FindAnnotation
import org.bukkit.configuration.file.YamlConfiguration
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * Annotate config class
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [ConfigSpec::class])
annotation class Config

/**
 * Configuration specification
 * @param name Name of config
 */
abstract class ConfigSpec(
    internal val name: String? = null
) {
    internal lateinit var path: String
    internal lateinit var yaml: YamlConfiguration
    internal fun initialize() {
        this.javaClass.kotlin.declaredMemberProperties.stream().forEach { it.get(this) }
    }

    /**
     * Define property with default value
     * @param name Name of this property
     * @param default Default value
     * @return Mutable property
     */
    protected fun <T : Any> require(name: String, default: T): ReadWriteProperty<ConfigSpec, T> {
        return ConfigProperty(default, name)
    }

    /**
     * Define property with default value.
     * Name of property is equivalent to property
     * @param default Default value
     * @return Mutable property
     */
    protected fun <T : Any> require(default: T): ReadWriteProperty<ConfigSpec, T> {
        return ConfigProperty(default)
    }

    /**
     * Define list of property.
     * @param name Name of property
     * @return Mutable Property
     */
    protected inline fun <reified T : Any> requireList(name: String): ReadWriteProperty<ConfigSpec, MutableList<T>> {
        return ListConfigProperty(T::class, name)
    }

    /**
     * Define list of property.
     * Name of property is equivalent to property
     * @return Mutable Property
     */
    protected inline fun <reified T : Any> requireList(): ReadWriteProperty<ConfigSpec, MutableList<T>> {
        return ListConfigProperty(T::class)
    }
}