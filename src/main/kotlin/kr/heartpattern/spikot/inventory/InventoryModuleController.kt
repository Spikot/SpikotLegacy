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

package kr.heartpattern.spikot.inventory

import kr.heartpattern.spikot.misc.AbstractMutableProperty
import kr.heartpattern.spikot.module.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

/**
 * Control [AbstractInventoryModule]
 * @param type Type of controlled module
 */
@BaseModule
@Module(ModulePriority.LOWEST)
abstract class InventoryModuleController<T : AbstractInventoryModule>(val type: KClass<T>) : AbstractModule() {
    object MutableInventoryProperty : AbstractMutableProperty<Inventory>(AbstractInventoryModule.InventoryProperty)

    private val modules = HashMap<UUID, ModuleHandler>()

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun InventoryOpenEvent.onOpen() {
        if (player.uniqueId in modules)
            throw IllegalStateException("Player has unloaded inventory module")

        val handler = ModuleHandler(type, plugin)
        handler.context[PlayerModuleController.MutablePlayerProperty] = player as Player
        handler.context[MutableInventoryProperty]

        handler.load()
        handler.enable()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun InventoryCloseEvent.onClose() {
        val handler = modules[player.uniqueId]
            ?: throw IllegalStateException("Player does not have loaded inventory module")

        if (handler.state == IModule.State.ENABLE) // Support self-disable
            handler.disable()
    }

    operator fun get(player: Player): T? {
        return this[player.uniqueId]
    }

    operator fun get(uuid: UUID): T? {
        @Suppress("UNCHECKED_CAST")
        return modules[uuid]?.module as? T
    }
}