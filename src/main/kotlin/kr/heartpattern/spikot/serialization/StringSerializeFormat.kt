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