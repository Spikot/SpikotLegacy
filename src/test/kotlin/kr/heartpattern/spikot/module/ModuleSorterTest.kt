/*
 * Copyright 2020 HeartPattern
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   imitations under the License.
 */

package kr.heartpattern.spikot.module

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Module
private class DefaultModule : AbstractModule()

@Module(priority = ModulePriority.SYSTEM)
private class SystemModule : AbstractModule()

@Module(priority = ModulePriority.API)
private class APIModule : AbstractModule()

@Module(priority = ModulePriority.LOWEST)
private class LowestModule : AbstractModule()

@Module(priority = ModulePriority.LOW)
private class LowModule : AbstractModule()

@Module(priority = ModulePriority.NORMAL)
private class NormalModule : AbstractModule()

@Module(priority = ModulePriority.HIGH)
private class HighModule : AbstractModule()

@Module(priority = ModulePriority.HIGHEST)
private class HighestModule : AbstractModule()

@BaseModule
@Module(priority = ModulePriority.HIGH, dependOn = [HighModule::class])
private open class BaselineModule : AbstractModule()

@Module
private class ImplementModule : BaselineModule()

@Module(priority = ModulePriority.HIGH)
@LoadBefore([ImplementModule::class])
class BeforeModule : AbstractModule()

@Module(priority = ModulePriority.NORMAL, dependOn = [HighestModule::class])
class ConflictModule : AbstractModule()

class ModuleSorterTest {
    @Test
    fun emptyTest() {
        val sorted = sortModuleDependencies(listOf())
        assertEquals(emptyList(), sorted)
    }

    @Test
    fun successTest() {
        val modules = listOf(
            APIModule::class,
            LowestModule::class,
            LowModule::class,
            NormalModule::class,
            DefaultModule::class,
            HighModule::class,
            BeforeModule::class,
            ImplementModule::class,
            HighestModule::class
        )
        val sorted = sortModuleDependencies(modules.shuffled())

        for (i in 0..2) {
            assertEquals(modules[i], sorted[i])
        }

        assertEquals(modules.subList(3, 5).toSet(), sorted.subList(3, 5).toSet())
        assertEquals(modules.subList(5, 8).toSet(), sorted.subList(5, 8).toSet())
        assertEquals(modules[8], sorted[8])
    }
}