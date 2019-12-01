package io.github.ReadyMadeProgrammer

import org.junit.jupiter.api.Test
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.test.assertTrue

class VersionAdapterTest {
    private val regex = Pattern.compile("(([(\\[])(\\S+))?(\\s*~\\s*)((\\S+)([)\\]]))?")
    val full = "(1.0.0-beta+exp.sha.5114f85 ~ 1.0.0-beta]"
    val frontHalf = "[1.0.0-beta+exp.sha.5114f85 ~"
    val endHalf = "~1.0.0-beta+exp.sha.5114f85)"
    val all = "all"

    val fullMatcher: Matcher
    val frontHalfMatcher: Matcher
    val endHalfMatcher: Matcher

    init {
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