package io.github.ReadyMadeProgrammer

import io.github.ReadyMadeProgrammer.Spikot.attachInvisible
import io.github.ReadyMadeProgrammer.Spikot.decryptInvisible
import io.github.ReadyMadeProgrammer.Spikot.encryptInvisible
import io.github.ReadyMadeProgrammer.Spikot.findInvisible
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class InvisibleTest {
    @Test
    fun test() {
        val random = Random()
        for (i in 1..100000) {
            check(random.nextInt(Int.MAX_VALUE))
        }
    }

    private fun check(code: Int) {
        try {
            assertEquals(code, code.encryptInvisible().decryptInvisible())
            assertEquals(code, "Hello".attachInvisible(code).findInvisible())
        } catch (e: Exception) {
            println(code)
            println(code.encryptInvisible())
        }
    }
}