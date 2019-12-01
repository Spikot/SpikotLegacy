package kr.heartpattern.spikot.misc

import kotlin.math.max
import kotlin.math.min

class Paginator<E>(
    val items: List<E>,
    val chunk: Int
) {
    val readableCurrentPage: Int
        get() = currentPage + 1
    val readableNextPage: Int
        get() = min(currentPage + 2, pageCount)
    val readablePreviousPage: Int
        get() = max(1, currentPage)
    var currentPage: Int = 0
        private set

    val pageCount = when {
        items.isEmpty() -> 1
        items.size % chunk == 0 -> items.size / chunk
        else -> items.size / chunk + 1
    }

    val currentItems: List<E>
        get() = items.subList(currentIndexs.first, currentIndexs.last + 1)

    val currentIndexs: IntRange
        get() = (currentPage * chunk) until (min((currentPage + 1) * chunk, items.size))

    fun next() {
        if (pageCount - 1 > currentPage) {
            currentPage++
        }
    }

    fun prev() {
        if (0 < currentPage) {
            currentPage--
        }
    }
}