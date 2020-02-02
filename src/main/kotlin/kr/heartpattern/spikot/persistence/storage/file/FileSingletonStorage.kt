/*
 * Copyright 2020 HeartPattern
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

package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.serialization.jsonSerializer
import java.io.File

class FileSingletonStorage<V>(
    val file: File,
    val serializer: KSerializer<V>
) : SingletonStorage<V> {
    override suspend fun get(): Option<V> {
        return if (file.exists()) {
            withContext(Dispatchers.IO) {
                Just(jsonSerializer.parse(serializer, file.readText()))
            }
        } else {
            None
        }
    }

    override suspend fun set(value: Option<V>) {
        withContext(Dispatchers.IO) {
            if (value is Just) {
                file.createNewFile()
                file.writeText(jsonSerializer.stringify(serializer, value.value))
            } else {
                file.delete()
            }
        }
    }
}