package kr.heartpattern.spikot.misc

import kotlin.math.max
import kotlin.math.min

/**
 * Paginate list of items
 * @param E Type of item
 * @param items List of items
 * @param chunk Chunk size
 */
class Paginator<E>(
    val items: List<E>,
    val chunk: Int
) {
    /**
     * Current page number with index start from 1
     */
    val readableCurrentPage: Int
        get() = currentPage + 1

    /**
     * Next page number with index start from 1. If current page is last page then return current page number
     */
    val readableNextPage: Int
        get() = min(currentPage + 2, pageCount)

    /**
     * Previous page number with index start from 1. If current page is first page then return current page number
     */
    val readablePreviousPage: Int
        get() = max(1, currentPage)

    /**
     * Current page number with index start from 0
     */
    var currentPage: Int = 0
        set(value) {
            field = when {
                value < 0 -> {
                    0
                }
                value > pageCount -> {
                    pageCount - 1
                }
                else -> {
                    value
                }
            }
        }

    /**
     * Number of page
     */
    val pageCount = when {
        items.isEmpty() -> 1
        items.size % chunk == 0 -> items.size / chunk
        else -> items.size / chunk + 1
    }

    /**
     * Current page item
     */
    val currentItems: List<E>
        get() = items.subList(currentIndexs.first, currentIndexs.last + 1)

    /**
     * Current page item's index
     */
    val currentIndexs: IntRange
        get() = (currentPage * chunk) until (min((currentPage + 1) * chunk, items.size))

    /**
     * Move to next page
     */
    fun next() {
        currentPage++
    }

    /**e
     * Move to previous page
     */
    fun prev() {
        currentPage--
    }
}