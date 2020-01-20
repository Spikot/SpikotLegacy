package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kr.heartpattern.spikot.misc.Just
import kr.heartpattern.spikot.misc.None
import kr.heartpattern.spikot.misc.Option
import kr.heartpattern.spikot.persistence.storage.SingletonStorage
import kr.heartpattern.spikot.serialization.SerializeType
import kr.heartpattern.spikot.serialization.jsonSerializer
import java.io.File

class FileSingletonStorage<V>(
    val file: File,
    val serializer: KSerializer<V>,
    val serializeType: SerializeType
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