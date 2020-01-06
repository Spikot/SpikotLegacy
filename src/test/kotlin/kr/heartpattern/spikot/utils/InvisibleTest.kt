package kr.heartpattern.spikot.utils

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Disabled
class InvisibleTest {
    @Suppress("SpellCheckingInspection")

    @Test
    fun test() {
        val random = Random()
        for (i in 1..100) {
            check(random.nextInt(Int.MAX_VALUE))
        }
        assertFailsWith(IllegalArgumentException::class) { "Hello".findInvisible() }
        assertFailsWith(IllegalArgumentException::class) { "Hello§r".findInvisible() }
        assertFailsWith(IllegalArgumentException::class) { "FLASD".decryptInvisible() }
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