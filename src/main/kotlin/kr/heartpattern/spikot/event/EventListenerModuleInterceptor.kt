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

package kr.heartpattern.spikot.event

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.module.IModuleInterceptor
import kr.heartpattern.spikot.module.ModuleHandler
import kr.heartpattern.spikot.module.ModuleInterceptor
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

/**
 * Register event handler in module
 */
@ModuleInterceptor
private object EventListenerModuleInterceptor : IModuleInterceptor {
    override fun onEnable(handler: ModuleHandler) {
        Bukkit.getPluginManager().registerEvents(handler.module!!, handler.owner)
    }

    override fun onDisable(handler: ModuleHandler) {
        HandlerList.unregisterAll(handler.module!!)
    }

    override fun onError(handler: ModuleHandler, state: IModule.State, throwable: Throwable) {
        HandlerList.unregisterAll(handler.module!!)
    }
}