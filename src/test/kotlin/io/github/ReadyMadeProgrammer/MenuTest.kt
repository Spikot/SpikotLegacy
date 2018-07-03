package io.github.ReadyMadeProgrammer

import io.github.ReadyMadeProgrammer.Spikot.guiBuilder.Menu
import kotlin.test.Test

class MenuTest {
    @Test
    fun BuildingTest() {

    }
}

class TestMenu : Menu() {
    init {
        config {
            title = "Hello"
            size = 54
            Closeable

        }
    }
}