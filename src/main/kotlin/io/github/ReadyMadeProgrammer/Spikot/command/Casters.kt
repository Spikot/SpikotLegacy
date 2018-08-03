package io.github.ReadyMadeProgrammer.Spikot.command

import io.github.ReadyMadeProgrammer.Spikot.utils.KPlayer
import org.bukkit.entity.Player
import java.util.*

fun <T> toInt(): Cast<T, Int> = { it.toString().toInt() }

fun <T> toDouble(): Cast<T, Double> = { it.toString().toDouble() }

fun <T> toString(): Cast<T, String> = { it.toString() }

fun toUuidPlayer(): Cast<UUID, Player> = { uuid: UUID -> KPlayer[uuid]!! }

inline fun <T, reified E : Enum<E>> toEnum(): Cast<T, E> = {
    val name = it.toString()
    val javaClass = E::class.java
    val result = javaClass.enumConstants.find { e -> e.name.equals(name, ignoreCase = true) }
    result ?: throw kotlin.ClassCastException("Cannot find matching enum")
}

fun <T, R> map(map: Map<T, R>): Cast<T, R> = { map[it] ?: throw ClassCastException("Cannot find matching item") }