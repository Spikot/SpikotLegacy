/*
 * Copyright 2020 Spikot project authors
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

package kr.heartpattern.spikot.module

import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

/**
 * Annotate module. Should annotated class which implement [kr.heartpattern.spikot.module.IModule]
 * It is recommended to set only one of priority or dependOn.
 * @param priority Load priority of module
 * @param dependOn Array of module which is required for this module
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [IModule::class])
annotation class Module(val priority: ModulePriority = ModulePriority.DEFAULT, val dependOn: Array<KClass<*>> = [])

/**
 * Annotate module should load before given modules
 * @param value Array of module which should be load after this module
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadBefore(val value: Array<KClass<*>> = [])

/**
 * Annotate module should load only when feature is enabled
 * @param value Required feature
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(val value: String)

/**
 * Annotate this module itself should not load. Module annotated with this annotation is base implementation for
 * api user.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseModule

/**
 * Annotate this module should not be loaded
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Disable

/**
 * Priority of module. Load and enable phase is done in sequence of system, api, lowest to highest. Disable phase is
 * done in reverse order of load.
 */
enum class ModulePriority {
    /**
     * Does not have priority. Priority order determined automatically by spikot.
     */
    DEFAULT,
    /**
     * Priority used by spikot internal system.
     */
    SYSTEM,
    /**
     * Priority used by module which provide base api.
     */
    API,
    /**
     * Priority for lowest layer module.
     */
    LOWEST,
    /**
     * Priority for low layer module.
     */
    LOW,
    /**
     * Priority for normal layer module.
     */
    NORMAL,
    /**
     * Priority for high layer module.
     */
    HIGH,
    /**
     * Priority for highest layer module.
     */
    HIGHEST
}