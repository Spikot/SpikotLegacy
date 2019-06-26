package io.github.ReadyMadeProgrammer.Spikot.misc

import java.util.*

interface Converter<B, F> {
    companion object {
        operator fun <B, F> invoke(read: (B) -> F, write: (F) -> B): Converter<B, F> {
            return object : Converter<B, F> {
                override fun read(raw: B): F {
                    return read(raw)
                }

                override fun write(converted: F): B {
                    return write(converted)
                }
            }
        }
    }

    fun read(raw: B): F
    fun write(converted: F): B
}

interface StringConverter<R> : Converter<R, String> {
    companion object {
        val UUID = object : StringConverter<UUID> {
            override fun read(raw: UUID): String {
                return raw.toString()
            }

            override fun write(converted: String): UUID {
                return java.util.UUID.fromString(converted)
            }
        }

        val STRING = object : StringConverter<String> {
            override fun read(raw: String): String {
                return raw
            }

            override fun write(converted: String): String {
                return converted
            }
        }

        val INT = object : StringConverter<Int> {
            override fun read(raw: Int): String {
                return raw.toString()
            }

            override fun write(converted: String): Int {
                return converted.toInt()
            }
        }
    }
}