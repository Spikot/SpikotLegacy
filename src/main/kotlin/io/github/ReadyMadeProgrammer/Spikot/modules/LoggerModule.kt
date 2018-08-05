package io.github.ReadyMadeProgrammer.Spikot.modules

import mu.KLogger
import mu.KotlinLogging
import org.koin.dsl.module.applicationContext
import org.koin.standalone.inject

@ExternalModule
class LoggerModule : ModuleConfig {
    override val module = applicationContext {
        factory { params -> KotlinLogging.logger(params.get<String>("prefix")) }
    }
}

fun Component.injectLogger(prefix: String) = inject<KLogger> { mapOf("prefix" to prefix) }
