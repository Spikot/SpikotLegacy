package kr.heartpattern.spikot.misc

import org.bukkit.command.CommandSender
import kotlin.math.log10

/**
 * Configure chat paginator
 */
class ChatPaginatorBuilder<E> {
    /**
     * Head of chat
     */
    var header: (Int, Int) -> String? = { curr, max -> "===========${curr + 1}/$max" }

    /**
     * Foot of chat
     */
    var footer: (Int, Int) -> String? = { curr, max -> "===========================" + "=".repeat(log10(curr.toDouble()).toInt() + log10(max.toDouble()).toInt()) }

    /**
     * Formatter to format each line
     */
    var format: (Int, Int, E) -> String = { index, _, content -> "$index. $content" }

    /**
     * Max length to chunk
     */
    var length = 10

    /**
     * List of data to paginate
     */
    var data: List<E> = emptyList()
}

/**
 * Chat paginator
 */
class ChatPaginator<E> internal constructor(
    private val header: (Int, Int) -> String?,
    private val footer: (Int, Int) -> String?,
    private val format: (Int, Int, E) -> String,
    private val length: Int,
    private val data: List<E>,
    private val invocationObserver: (List<String>)->Unit
) {
    /**
     * Length of page
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val size
        get() = (data.size - 1) / length + 1

    /**
     * Get page
     * @param page Page to get
     * @return List of current page chat
     */
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

/**
 * Create ChatPaginator
 * @param body lambda that configure chat paginator
 * @return ChatPaginator instance
 */
@Suppress("unused")
fun <E> paginate(body: ChatPaginatorBuilder<E>.() -> Unit): ChatPaginator<E> {
    return paginate(body) {}
}

/**
 * Create ChatPaginator with observer to capture page get operation
 * @param body lambda that configure chat paginator
 * @param observer observer that capture page get operation
 * @return ChatPaginator instance
 */
private fun <E> paginate(body: ChatPaginatorBuilder<E>.() -> Unit, observer: (List<String>)->Unit): ChatPaginator<E> {
    val builder = ChatPaginatorBuilder<E>()
    builder.body()
    return ChatPaginator(builder.header, builder.footer, builder.format, builder.length, builder.data, observer)
}

/**
 * Send paginated chat to command sender
 * @receiver CommandSender to receive chat
 * @param body lambda that configure chat paginator
 * @return ChatPaginator instance which send chat when invoked
 */
@Suppress("unused")
fun <E> CommandSender.sendPage(body: ChatPaginatorBuilder<E>.() -> Unit): ChatPaginator<E> {
    return paginate(body) { it.forEach(this::sendMessage) }
}