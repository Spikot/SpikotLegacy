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

/**
 * Module interceptor which intercept each step of module lifecycle.
 */
interface IModuleInterceptor {
    /**
     * Intercept module create
     * @param handler Target module handler
     */
    fun onCreate(handler: ModuleHandler) {}

    /**
     * Intercept module load
     * @param handler Target module handler
     */
    fun onLoad(handler: ModuleHandler) {}

    /**
     * Intercept module enable
     * @param handler Target module handler
     */
    fun onEnable(handler: ModuleHandler) {}

    /**
     * Intercept module disable
     * @param handler Target module handler
     */
    fun onDisable(handler: ModuleHandler) {}

    /**
     * Intercept module error
     * @param handler Target module handler
     * @param state State of module
     * @param throwable Thrown error
     */
    fun onError(handler: ModuleHandler, state: IModule.State, throwable: Throwable) {}
}