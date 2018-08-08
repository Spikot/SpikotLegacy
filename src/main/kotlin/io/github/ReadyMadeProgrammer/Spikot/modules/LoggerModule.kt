package io.github.ReadyMadeProgrammer.Spikot.modules

import mu.KLogger
import mu.KotlinLogging
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import org.koin.standalone.inject

@ExternalModule
class LoggerModule : ModuleConfig {
    override val module = module {
        factory { params -> KotlinLogging.logger(params.get<String>(0)) }
    }
}

fun Component.injectLogger(prefix: String) = inject<KLogger> { parametersOf(prefix) }
