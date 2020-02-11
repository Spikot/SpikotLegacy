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

package kr.heartpattern.spikot

import kr.heartpattern.spikot.plugin.FindAnnotation
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.catchAll
import kr.heartpattern.spikot.utils.getInstance
import mu.KotlinLogging
import kotlin.reflect.full.findAnnotation

/**
 * Annotate bootstrap class
 * @param loadOrder Load order of class. Lower run faster
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@FindAnnotation(impl = [IBootstrap::class])
annotation class Bootstrap(val loadOrder: Int = 0)

/**
 * Server bootstrapping class which call on load state
 */
interface IBootstrap {
    /**
     * Call when server load
     */
    fun onLoad() {}

    /**
     * Call when server start
     */
    fun onStartup() {}

    /**
     * Call when server shutdown
     */
    fun onShutdown() {}
}

/**
 * Call bootstrap classes
 */
internal object BootstrapManager : IBootstrap {
    private val logger = KotlinLogging.logger{}
    private lateinit var instances: List<IBootstrap>

    override fun onStartup() {
        instances = SpikotPluginManager
            .annotationIterator<Bootstrap>()
            .asSequence()
            .map { (type) -> logger.catchAll("Cannot create bootstrap instance") { type.getInstance() as IBootstrap? } }
            .filterNotNull()
            .sortedBy {
                it::class.findAnnotation<Bootstrap>()?.loadOrder
                    ?: throw IllegalStateException("${it::class.simpleName} does not have bootstrap annotation")
            }
            .toList()

        for (instance in instances) {
            logger.catchAll("Cannot load bootstrap") {
                logger.debug{"Load bootstrap ${instance::class.simpleName}"}
                instance.onLoad()
            }
        }

        for (instance in instances) {
            logger.catchAll("Cannot start bootstrap") {
                logger.debug{"Enable bootstrap ${instance::class.simpleName}"}
                instance.onStartup()
            }
        }
    }

    override fun onShutdown() {
        for (bootstrap in instances.reversed()) {
            logger.catchAll("Cannot shutdown bootstrap") {
                logger.debug{"Shutdown bootstrap ${bootstrap::class.simpleName}"}
                bootstrap.onShutdown()
            }
        }
    }
}