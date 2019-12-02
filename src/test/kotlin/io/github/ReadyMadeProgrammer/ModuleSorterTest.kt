package io.github.ReadyMadeProgrammer

import kr.heartpattern.spikot.module.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Module
@LoadBefore([IModule::class, FirstMiddleModule::class])
class FirstFirstModule : AbstractModule()

@Module
@LoadBefore([IModule::class])
class FirstMiddleModule : AbstractModule()

@Module(depend = [FirstMiddleModule::class])
@LoadBefore([IModule::class])
class FirstLastModule : AbstractModule()

@Module
@LoadBefore([MiddleMiddleModule::class])
class MiddleFirstModule : AbstractModule()

@Module
class MiddleMiddleModule : AbstractModule()

@Module(depend = [MiddleMiddleModule::class])
class MiddleLastModule : AbstractModule()

@Module(depend = [IModule::class])
@LoadBefore([LastMiddleModule::class])
class LastFirstModule : AbstractModule()

@Module(depend = [IModule::class])
class LastMiddleModule : AbstractModule()

@Module(depend = [IModule::class, LastMiddleModule::class])
class LastLastModule : AbstractModule()

@Module(depend = [MiddleLastModule::class])
@LoadBefore([MiddleFirstModule::class])
class CircularModule : AbstractModule()


class ModuleSorterTest {
    @Test
    fun successTest() {
        val sorted = sortModuleDependencies(listOf(
            LastLastModule::class,
            MiddleMiddleModule::class,
            FirstLastModule::class,
            LastMiddleModule::class,
            LastFirstModule::class,
            MiddleFirstModule::class,
            FirstFirstModule::class,
            MiddleLastModule::class,
            FirstMiddleModule::class
        ))

        assertEquals(
            sorted,
            listOf(
                FirstFirstModule::class,
                FirstMiddleModule::class,
                FirstLastModule::class,
                MiddleFirstModule::class,
                MiddleMiddleModule::class,
                MiddleLastModule::class,
                LastFirstModule::class,
                LastMiddleModule::class,
                LastLastModule::class
            )
        )
    }

    @Test
    fun failTest() {
        assertThrows(UninitializedPropertyAccessException::class.java) {
            // Access to spikot(to show error message) produce this.
            sortModuleDependencies(listOf(
                LastLastModule::class,
                MiddleMiddleModule::class,
                FirstLastModule::class,
                LastMiddleModule::class,
                LastFirstModule::class,
                MiddleFirstModule::class,
                FirstFirstModule::class,
                MiddleLastModule::class,
                FirstMiddleModule::class,
                CircularModule::class
            ))
        }
    }
}