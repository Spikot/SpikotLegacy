package kr.heartpattern.spikot.misc

import kotlin.math.min

fun <E> List<E>.chunk(unit: Int, index: Int): List<E> {
    if (size <= unit * index) return emptyList()
    return subList(unit * index, min(unit * (index + 1), size))
}