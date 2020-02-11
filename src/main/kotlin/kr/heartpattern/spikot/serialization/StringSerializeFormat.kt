package kr.heartpattern.spikot.serialization

import kotlinx.serialization.StringFormat

abstract class StringSerializeFormat(val fileExtensionName: String) {
    companion object;
    abstract val serializer: StringFormat
}

val StringSerializeFormat.Companion.JSON
    get() = JsonStringSerializeFormat

object JsonStringSerializeFormat : StringSerializeFormat("json") {
    override val serializer: StringFormat = jsonSerializer
}

val StringSerializeFormat.Companion.YAML
    get() = YamlStringSerializeFormat

object YamlStringSerializeFormat : StringSerializeFormat("yml") {
    override val serializer: StringFormat = yamlSerializer
}