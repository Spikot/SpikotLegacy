package kr.heartpattern.spikot.misc.works

interface Transform<T, U> {
    companion object {
        operator fun <T, U> invoke(before: T, after: U): Transform<T, U> {
            return SimpleTransform(before, after)
        }
    }

    val before: T
    val after: U

    data class SimpleTransform<T, U>(override val before: T, override val after: U) : Transform<T, U>
}

interface MutableTransform<T, U> : Transform<T, U> {
    companion object {
        operator fun <T, U> invoke(before: T, after: U): MutableTransform<T, U> {
            return SimpleMutableTransform(before, after)
        }
    }

    override var after: U

    data class SimpleMutableTransform<T, U>(override val before: T, override var after: U) : MutableTransform<T, U>
}