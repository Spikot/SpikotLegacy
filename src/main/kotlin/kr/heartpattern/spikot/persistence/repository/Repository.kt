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

package kr.heartpattern.spikot.persistence.repository

import kr.heartpattern.spikot.module.*
import kr.heartpattern.spikot.persistence.storage.Storage

/**
 * Represent data repository
 */
interface Repository<S : Storage> : IModule

abstract class AbstractRepository<S : Storage> : AbstractModule(), Repository<S> {
    abstract val storage: S
    override fun onLoad() {
        storage.create(plugin)
        storage.load()
    }

    override fun onEnable() {
        storage.enable()
    }

    override fun onDisable() {
        storage.disable()
    }
}