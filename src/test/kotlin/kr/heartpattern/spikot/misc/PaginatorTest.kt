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

package kr.heartpattern.spikot.misc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PaginatorTest {
    lateinit var paginator: Paginator<Int>

    @BeforeEach
    fun beforeEach() {
        paginator = Paginator((1..100).toList(), 10)
    }

    @Test
    fun testMin() {
        paginator.prev()
        paginator.prev()
        assertEquals(0, paginator.currentPage)
        paginator.currentPage = -4
        assertEquals(0, paginator.currentPage)
    }

    @Test
    fun testMax() {
        paginator.currentPage = 9
        paginator.next()
        paginator.next()
        assertEquals(9, paginator.currentPage)
        paginator.currentPage = 25
        assertEquals(9, paginator.currentPage)
    }

    @Test
    fun testValues() {
        paginator.currentPage = 4
        assertEquals(4, paginator.readablePreviousPage)
        assertEquals(6, paginator.readableNextPage)
        assertEquals(5, paginator.readableCurrentPage)
        assertEquals(4, paginator.currentPage)
        assertEquals(40..49, paginator.currentIndexs)
        assertEquals((41..50).toList(), paginator.currentItems)
    }
}