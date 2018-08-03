package io.github.ReadyMadeProgrammer.Spikot.modules

import mu.KotlinLogging
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

class LoggerModule : ModuleConfig {
    override val module: Module
        get() = applicationContext {
            factory { params -> KotlinLogging.logger(params.get<String>("prefix")) }
        }
}