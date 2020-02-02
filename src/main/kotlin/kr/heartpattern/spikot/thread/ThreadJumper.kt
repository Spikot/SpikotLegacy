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

@file:Suppress("unused")

package kr.heartpattern.spikot.thread

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * Run lambda in sync thread
 * @receiver Plugin to run this task
 * @param block lambda to run in sync thread
 */
fun Plugin.runSync(block: () -> Unit) {
    if (Bukkit.isPrimaryThread()) { // Fast routine
        block()
    } else {
        Bukkit.getScheduler().runTask(this, block)
    }
}

/**
 * Run lambda at next tick in sync thread
 * @receiver Plugin to run this task
 * @param block lambda to run at next tick in sync thread
 */
fun Plugin.runNextSync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskLater(this, block, 1)
}

/**
 * Run lambda in async thread
 * @receiver Plugin to run this task
 * @param block lambda to run in async thread
 */
fun Plugin.runAsync(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(this, block)
}