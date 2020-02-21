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

import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.module.ServerModule
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

@ServerModule(priority = ModulePriority.API)
internal object TickEventEmitter : AbstractModule() {
    override fun onEnable() {
        var tickCount = 0
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            TickEvent(tickCount++).execute()
        }, 1L, 1L)
    }
}

/**
 * Event which fire every thick
 * @param tick Number of tick that TickEvent fired.
 */
class TickEvent(val tick: Int) : Event() {
    companion object {
        @JvmField
        val handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}
