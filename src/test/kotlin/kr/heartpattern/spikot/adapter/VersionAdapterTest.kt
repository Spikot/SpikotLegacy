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

package kr.heartpattern.spikot.adapter

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