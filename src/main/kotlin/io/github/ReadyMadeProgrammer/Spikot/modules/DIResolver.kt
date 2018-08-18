package io.github.ReadyMadeProgrammer.Spikot.modules

import io.github.ReadyMadeProgrammer.Spikot.ServerVersion
import io.github.ReadyMadeProgrammer.Spikot.Version
import io.github.ReadyMadeProgrammer.Spikot.command.CommandManager
import io.github.ReadyMadeProgrammer.Spikot.logger
import io.github.ReadyMadeProgrammer.Spikot.spikotPlugin
import io.github.ReadyMadeProgrammer.Spikot.utils.version
import org.koin.dsl.module.Module
import org.koin.log.EmptyLogger
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.jvm.jvmName

/**
 * This make us use koin without defining applicationContext
 * DIResolver scan class annotated with Service or Contract and define applicationContext itself.
 */
object DIResolver {
    private data class ServiceWrapper(val klass: KClass<*>, val name: String, val singleton: Boolean)

    private val rawContracts = mutableSetOf<Class<*>>()
    private val rawServices = mutableSetOf<Class<*>>()
    private val rawModuleConfig = mutableSetOf<Class<out ModuleConfig>>()
    private val rawCommands = mutableSetOf<Class<*>>()

    private var module: Module? = null
    private val externalModule = mutableSetOf<Module>()
    internal val moduleInstances = mutableSetOf<io.github.ReadyMadeProgrammer.Spikot.modules.Module>()
    internal val modules = mutableSetOf<KClass<out io.github.ReadyMadeProgrammer.Spikot.modules.Module>>()
    internal val feature = mutableSetOf<String>()
    internal fun load() {
        logger.info { "DI loading start" }
        loadClasses()
        val contracts = rawContracts.filter {
            if (it.isInterface) {
                true
            } else {
                logger.warn { "Contract ${it.simpleName} is not interface" }
                false
            }
        }.map { it.kotlin }
        val services = mutableMapOf<String, ServiceWrapper>()
        val standalone = mutableSetOf<ServiceWrapper>()
        rawCommands.forEach {
            CommandManager.addCommand(it)
        }
        rawServices.map { it.kotlin }.filter {
            Component::class.isSuperclassOf(it)
        }.filter {
            val feature = it.findAnnotation<Feature>()
            if (feature != null) {
                this.feature.contains(feature.feature) == feature.enable
            } else {
                true
            }
        }.filter { clz ->
            if (clz.findAnnotation<Adapter>() == null) true
            else clz.annotations.filter { it is Adapter }
                    .any { version.match((it as Adapter).platform, it.version) == ServerVersion.Result.COMPACT }
        }.forEach { k ->
            val singleton = k.findAnnotation<Singleton>() != null
            val name = k.findAnnotation<Service>()!!.name
            val contract = contracts.find { it.isSuperclassOf(k) }
            val serviceWrapper = ServiceWrapper(k, name, singleton)
            if (contract != null) {
                val registered = services[contract.jvmName]
                if (registered != null) {
                    val registeredAdapter = registered.klass.findAnnotation<Adapter>()
                    val adapter = k.findAnnotation<Adapter>()
                    if (adapter == null) {
                        standalone.add(serviceWrapper)
                    } else {
                        if (registeredAdapter == null) {
                            standalone.add(registered)
                            services[contract.jvmName] = serviceWrapper
                        } else {
                            var registeredBest = Version(registeredAdapter.version[0])
                            registeredAdapter.version.map { Version(it) }.forEach {
                                if (version.version.closer(registeredBest, it) < 0) {
                                    registeredBest = it
                                }
                            }
                            var best = Version(adapter.version[0])
                            adapter.version.map { Version(it) }.forEach {
                                if (version.version.closer(best, it) < 0) {
                                    best = it
                                }
                            }
                            if (version.version.closer(best, registeredBest) > 0) {
                                standalone.add(registered)
                                services[contract.jvmName] = serviceWrapper
                            } else {
                                standalone.add(serviceWrapper)
                            }
                        }
                    }
                } else {
                    services[contract.jvmName] = serviceWrapper
                }
            } else {
                standalone.add(serviceWrapper)
            }
            if (io.github.ReadyMadeProgrammer.Spikot.modules.Module::class.isSuperclassOf(k)) {
                @Suppress("UNCHECKED_CAST")
                modules.add(k as KClass<out io.github.ReadyMadeProgrammer.Spikot.modules.Module>)
            }
        }
        externalModule.addAll(rawModuleConfig.map { it.kotlin.createInstance().module })
        logger.info { "Load ${contracts.size} contracts, ${services.size + standalone.size} services, ${modules.size} modules, ${externalModule.size} module configs" }
        module = org.koin.dsl.module.module {
            contracts.forEach { k ->
                val service = services[k.jvmName]
                if (service == null) {
                    logger.warn { "Cannot find service match to contract: ${k.jvmName}" }
                    return@forEach
                }
                if (service.singleton) {
                    single(name = service.name) { service.klass.createInstance() } bind k
                } else {
                    factory(name = service.name) { service.klass.createInstance() } bind k
                }
            }
            standalone.forEach { s ->
                if (s.singleton) {
                    single(name = s.name) { s.klass.createInstance() }
                } else {
                    factory(name = s.name) { s.klass.createInstance() }
                }
            }
            modules.forEach { s ->
                factory(name = s.java.simpleName) { moduleInstances.find { ins -> s.isInstance(ins) }!! }
            }
        }
        val all = mutableListOf(module!!)
        all.addAll(externalModule)
        startKoin(all, logger = EmptyLogger())
    }

    private fun loadClasses() {
        spikotPlugin.dataFolder.parentFile.listFiles()
                .filter { it.name.endsWith(".jar") }
                .map { JarFile(it) }
                .forEach {
                    try {
                        it.getInputStream(it.getJarEntry("contracts")).bufferedReader().lines().forEach { name ->
                            val clazz = Class.forName(name)
                            rawContracts.add(clazz)
                        }
                        it.getInputStream(it.getJarEntry("services")).bufferedReader().lines().forEach { name ->
                            val clazz = Class.forName(name)
                            rawServices.add(clazz)
                        }
                        it.getInputStream(it.getJarEntry("modules")).bufferedReader().lines().forEach { name ->
                            val clazz = Class.forName(name)
                            @Suppress("UNCHECKED_CAST")
                            rawModuleConfig.add(clazz as Class<out ModuleConfig>)
                        }
                        it.getInputStream(it.getJarEntry("commands")).bufferedReader().lines().forEach { name ->
                            val clazz = Class.forName(name)
                            rawCommands.add(clazz)
                        }
                        logger.info { "Load ${it.name}" }
                    } catch (e: Throwable) {
                        //Nothing
                    }
                }
    }
}

inline fun <reified T : io.github.ReadyMadeProgrammer.Spikot.modules.Module> Component.injectModule(): Lazy<T> = lazy { inject<io.github.ReadyMadeProgrammer.Spikot.modules.Module>(T::class.java.simpleName).value as T }