package io.github.ReadyMadeProgrammer

import kr.heartpattern.spikot.misc.Paginator
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