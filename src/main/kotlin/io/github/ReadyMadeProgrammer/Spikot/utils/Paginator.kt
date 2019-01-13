package io.github.ReadyMadeProgrammer.Spikot.utils

import io.github.ReadyMadeProgrammer.Spikot.misc.Observer
import org.bukkit.command.CommandSender
import kotlin.math.log10

class PaginatorBuilder<E> {
    var header: (Int, Int) -> String? = { curr, max -> "===========${curr + 1}/$max" }
    var footer: (Int, Int) -> String? = { curr, max -> "===========================" + "=".repeat(log10(curr.toDouble()).toInt() + log10(max.toDouble()).toInt()) }
    var format: (Int, Int, E) -> String = { index, _, content -> "$index. $content" }
    var length = 10
    var data: List<E> = emptyList()
}

class Paginator<E> internal constructor(
        private val header: (Int, Int) -> String?,
        private val footer: (Int, Int) -> String?,
        private val format: (Int, Int, E) -> String,
        private val length: Int,
        private val data: List<E>,
        private val invocationObserver: Observer<List<String>>
) {
    @Suppress("MemberVisibilityCanBePrivate")
    val size
        get() = (data.size - 1) / length + 1

    operator fun invoke(page: Int): List<String> {
        check(page < size) { "page > size: $page $size" }
        val processedHeader = header(page, size)
        val processedFooter = footer(page, size)
        val count = if (processedHeader == null) 0 else 1 + if (processedFooter == null) 0 else 1
        val list = ArrayList<String>(count + 1 + (data.size - 1) / length)
        if (processedHeader != null) {
            list.add(processedHeader)
        }
        for (index in ((page - 1) * length) until (page * length)) {
            list.add(format(index, size, data[index]))
        }
        if (processedFooter != null) {
            list.add(processedFooter)
        }
        invocationObserver(list)
        return list
    }
}

@Suppress("unused")
fun <E> paginate(body: PaginatorBuilder<E>.() -> Unit): Paginator<E> {
    return paginate(body) {}
}

private fun <E> paginate(body: PaginatorBuilder<E>.() -> Unit, observer: Observer<List<String>>): Paginator<E> {
    val builder = PaginatorBuilder<E>()
    builder.body()
    return Paginator(builder.header, builder.footer, builder.format, builder.length, builder.data, observer)
}

@Suppress("unused")
fun <E> CommandSender.sendPage(body: PaginatorBuilder<E>.() -> Unit): Paginator<E> {
    return paginate(body) { it.forEach(this::sendMessage) }
}