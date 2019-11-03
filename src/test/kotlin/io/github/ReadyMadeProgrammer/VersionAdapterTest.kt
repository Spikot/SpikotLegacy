package io.github.ReadyMadeProgrammer

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.test.assertTrue

class VersionAdapterTest {
    private val regex = Pattern.compile("((\\(\\[)\\S+)?(\\w+,\\w+)+(\\S+(\\)]))?")
    val full = "(1.0.0-beta+exp.sha.5114f85 , 1.0.0-beta]"
    val frontHalf = "[1.0.0-beta+exp.sha.5114f85"
    val endHalf = "1.0.0-beta+exp.sha.5114f85)"
    val all = "all"

    lateinit var fullMatcher: Matcher
    lateinit var frontHalfMatcher: Matcher
    lateinit var endHalfMatcher: Matcher

    @BeforeAll
    fun beforeAll() {
        fullMatcher = regex.matcher(full)
        frontHalfMatcher = regex.matcher(frontHalf)
        endHalfMatcher = regex.matcher(endHalf)
    }

    @Test
    fun testMatch() {
        assertTrue(fullMatcher.matches())
        assertTrue(frontHalfMatcher.matches())
        assertTrue(endHalfMatcher.matches())
    }
}