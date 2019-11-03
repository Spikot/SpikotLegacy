package kr.heartpattern.spikot.misc

import kr.heartpattern.spikot.misc.UnboundInterval.*

sealed class UnboundInterval<in T : Comparable<T>> {
    class NegativeInfinite<T : Comparable<T>> : UnboundInterval<T>()
    class PositiveInfinite<T : Comparable<T>> : UnboundInterval<T>()
    data class Open<T : Comparable<T>>(val value: T) : UnboundInterval<T>()
    data class Close<T : Comparable<T>>(val value: T) : UnboundInterval<T>()
}

class Interval<T : Comparable<T>>(
    val start: UnboundInterval<T>,
    val end: UnboundInterval<T>
) {
    operator fun contains(value: T): Boolean {
        val startAccept = when (start) {
            is NegativeInfinite -> true
            is PositiveInfinite -> false
            is Open -> start.value < value
            is Close -> start.value <= value
        }

        val endAccept = when (end) {
            is NegativeInfinite -> false
            is PositiveInfinite -> true
            is Open -> end.value > value
            is Close -> end.value >= value
        }
        return startAccept && endAccept
    }

    operator fun contains(interval: Interval<T>): Boolean {
        val startAccept = when (start) {
            is NegativeInfinite -> true
            is PositiveInfinite -> interval.start is PositiveInfinite
            else -> when (interval.start) {
                is NegativeInfinite -> start is NegativeInfinite
                is PositiveInfinite -> true
                else -> when {
                    start is Open && interval.start is Open -> start.value <= interval.start.value
                    start is Open && interval.start is Close -> start.value < interval.start.value
                    start is Close && interval.start is Open -> start.value <= interval.start.value
                    start is Close && interval.start is Close -> start.value <= interval.start.value
                    else -> error("Cannot be occur")
                }
            }
        }

        val endAccept = when (end) {
            is NegativeInfinite -> interval.end is NegativeInfinite
            is PositiveInfinite -> true
            else -> when (interval.end) {
                is NegativeInfinite -> true
                is PositiveInfinite -> end is PositiveInfinite
                else -> when {
                    end is Open && interval.end is Open -> end.value > interval.end.value
                    end is Open && interval.end is Close -> end.value >= interval.end.value
                    end is Close && interval.end is Open -> end.value >= interval.end.value
                    end is Close && interval.end is Close -> end.value >= interval.end.value
                    else -> error("Cannot be occur")
                }
            }
        }
        return startAccept && endAccept
    }
}