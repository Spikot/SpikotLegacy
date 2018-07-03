package io.github.ReadyMadeProgrammer

import io.github.ReadyMadeProgrammer.Spikot.ServerVersion
import io.github.ReadyMadeProgrammer.Spikot.ServerVersion.Platform.*
import io.github.ReadyMadeProgrammer.Spikot.Version
import org.apache.commons.lang.math.RandomUtils
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VersionTest {
    @Test
    fun parsingTest() {
        for (i in 1..10000) {
            val first = RandomUtils.nextInt().absoluteValue
            val second = RandomUtils.nextInt().absoluteValue
            val third = RandomUtils.nextInt().absoluteValue
            val variation = listOf(Version.Variation.NONE, Version.Variation.UPPER, Version.Variation.LOWER).shuffled()[0]
            assertEquals(Version(first, second, third, variation), Version("$first.$second.$third${variation.code}"))
            assertEquals("$first.$second.$third${variation.code}", Version("$first.$second.$third${variation.code}").toString())
            assertFailsWith(IllegalArgumentException::class) { Version("-12.31.224") }
        }
    }

    @Test
    fun compareTest() {
        var a = Version("3.1.2-")
        var b = Version("3.0.2")
        assertTrue(a.match(b))
        assertTrue(b.match(a))
        a = Version("3.1.2+")
        b = Version("4.0.2")
        assertTrue(a.match(b))
        assertTrue(b.match(a))
        a = Version("3.1.2")
        b = Version("3.1.2")
        assertTrue(a.match(b))
        a = Version("3.1.2-")
        b = Version("4.0.2")
        assertFalse(a.match(b))
        assertFalse(b.match(a))
        a = Version("3.1.2+")
        b = Version("3.0.2")
        assertFalse(a.match(b))
        assertFalse(b.match(a))
        a = Version("3.1.2")
        b = Version("1.2.3")
        assertFalse(a.match(b))
    }

    @Test
    fun platformEqualsTest() {
        val base = ServerVersion(CRAFT, Version("3.1.2"))
        assertEquals("CRAFT-3.1.2", base.toString())
        assertEquals(ServerVersion.Result.COMPACT, base.match(arrayOf(GLOWSTONE, CRAFT), arrayOf("2.0.1+", "1.2.3")))
        assertEquals(ServerVersion.Result.PLATFORM_ERROR, base.match(arrayOf(PAPER, SPIGOT), arrayOf("2.0.1+", "1.2.3")))
        assertEquals(ServerVersion.Result.VERSION_ERROR, base.match(arrayOf(GLOWSTONE, CRAFT), arrayOf("2.0.1-", "1.2.3")))
    }
}