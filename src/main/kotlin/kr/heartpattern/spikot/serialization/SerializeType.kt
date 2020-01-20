package kr.heartpattern.spikot.serialization

import kotlinx.serialization.KSerializer

sealed class SerializeType(val fileExtensionName: String) {
    object JSON : SerializeType("json") {
        override fun <T> serialize(serializer: KSerializer<T>, value: T): String {
            return jsonSerializer.stringify(serializer, value)
        }

        override fun <T> deserialize(serializer: KSerializer<T>, text: String): T {
            return jsonSerializer.parse(serializer, text)
        }
    }

    object YAML : SerializeType("yml") {
        override fun <T> serialize(serializer: KSerializer<T>, value: T): String {
            return yamlSerializer.stringify(serializer, value)
        }

        override fun <T> deserialize(serializer: KSerializer<T>, text: String): T {
            return yamlSerializer.parse(serializer, text)
        }
    }


    abstract fun <T> serialize(serializer: KSerializer<T>, value: T): String
    abstract fun <T> deserialize(serializer: KSerializer<T>, text: String): T
}