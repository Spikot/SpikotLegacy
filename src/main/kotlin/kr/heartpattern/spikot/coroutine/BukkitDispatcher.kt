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

package kr.heartpattern.spikot.coroutine

import com.google.auto.service.AutoService
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.MainDispatcherFactory
import kr.heartpattern.spikot.spikot
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
@UseExperimental(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val Dispatchers.Bukkit: DispatcherBukkit
    get() = BukkitDispatcher

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
sealed class DispatcherBukkit : MainCoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Bukkit.getScheduler().runTask(spikot, block)
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = Bukkit.getScheduler().runTaskLater(spikot, {
            with(continuation) {
                resumeUndispatched(Unit)
            }
        }, timeMillis / 50)
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        val task = Bukkit.getScheduler().runTaskLater(spikot, block, timeMillis / 50)
        return object : DisposableHandle {
            override fun dispose() {
                task.cancel()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AutoService(MainDispatcherFactory::class)
internal class BukkitDispatcherFactory : MainDispatcherFactory {
    override val loadPriority: Int
        get() = 0

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher = BukkitDispatcher
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
private object ImmediateBukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = this

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = !Bukkit.isPrimaryThread()

    override fun toString() = "Bukkit [immediate]"
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal object BukkitDispatcher : DispatcherBukkit() {
    override val immediate: MainCoroutineDispatcher
        get() = ImmediateBukkitDispatcher

    override fun toString() = "Bukkit"
}

suspend fun delayTick(tick: Long) {
    delay(tick * 50)
}