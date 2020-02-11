/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.misc

/**
 * List that convert type lazily
 * @param B Back type
 * @param F Front type
 * @param backingList Back list with back type
 * @param converter Converter that convert between back and front type
 */
open class ConvertingList<B, F>(
    open val backingList: List<B>,
    protected val converter: Converter<B, F>
) : List<F> {
    protected fun read(value: B): F {
        return converter.read(value)
    }

    protected fun write(value: F): B {
        return converter.write(value)
    }

    override val size: Int
        get() = backingList.size

    override fun contains(element: F): Boolean = backingList.contains(write(element))

    override fun containsAll(elements: Collection<F>): Boolean = backingList.containsAll(elements.map(::write))

    override fun get(index: Int): F = read(backingList.get(index))

    override fun indexOf(element: F): Int = backingList.indexOf(write(element))

    override fun isEmpty(): Boolean = backingList.isEmpty()

    override fun iterator(): Iterator<F> = object : Iterator<F> {
        val iterator = backingList.iterator()

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): F = read(iterator.next())
    }

    override fun lastIndexOf(element: F): Int = backingList.lastIndexOf(write(element))

    override fun listIterator(): ListIterator<F> = object : ListIterator<F> {
        val iterator = backingList.listIterator()

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun hasPrevious(): Boolean = iterator.hasPrevious()

        override fun next(): F = read(iterator.next())

        override fun nextIndex(): Int = iterator.nextIndex()

        override fun previous(): F = read(iterator.previous())

        override fun previousIndex(): Int = iterator.previousIndex()

    }

    override fun listIterator(index: Int): ListIterator<F> = object : ListIterator<F> {
        val iterator = backingList.listIterator(index)

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun hasPrevious(): Boolean = iterator.hasPrevious()

        override fun next(): F = read(iterator.next())

        override fun nextIndex(): Int = iterator.nextIndex()

        override fun previous(): F = read(iterator.previous())

        override fun previousIndex(): Int = iterator.previousIndex()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<F> = ConvertingList<B, F>(backingList.subList(fromIndex, toIndex), converter)
}