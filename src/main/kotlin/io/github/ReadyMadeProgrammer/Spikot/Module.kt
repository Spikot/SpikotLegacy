package io.github.ReadyMadeProgrammer.Spikot

import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object ModuleManager {
    private val modules = HashMap<String, MutableSet<Pair<ModuleWrapper<*>, Annotation>>>()
    private val lifeCycles = HashMap<String, ModuleLifeCycle<*, *>>()
    internal fun load() {
        println("Module Load Start")
        Reflections().getSubTypesOf(Module::class.java).iterator().asSequence().map { it.kotlin }
                .filter { !it.disabled }
                .filterNot { c -> modules.any { (_, v) ->
                    v.any { (vv, _) -> vv.module == c } } }
                .forEach {
                    val clazz = it as KClass<out Module<*>>
                    println("Find ${it.simpleName}")
                    val annotation = it.scope ?: return@forEach
                    val set = modules[annotation.annotationClass.simpleName]
                    val moduleWrapper = ModuleWrapper(clazz) as ModuleWrapper<*>
                    if (set == null) modules[annotation.annotationClass.simpleName!!] = mutableSetOf(Pair(moduleWrapper, annotation))
                    else set.add(Pair(moduleWrapper, annotation))
                }
    }

    fun <T, A : Annotation> addModuleLifeCycle(scope: KClass<A>, lifeCycle: ModuleLifeCycle<T, A>) {
        lifeCycles[scope.simpleName!!] = lifeCycle
        modules[scope.simpleName!!]?.forEach {
            lifeCycle.addModule(it.second as A, it.first as ModuleWrapper<T>)
        }
    }
}

class ModuleWrapper<T>(internal val module: KClass<out Module<T>>) {
    var moduleInstance: Module<T>? = null
        private set

    fun enable(information: T) {
        moduleInstance = module.objectInstance ?: module.createInstance()
        moduleInstance!!.onModuleEnable(information)
    }

    fun disable() {
        if (moduleInstance == null) throw IllegalStateException("Disable not enabled module")
        moduleInstance!!.onModuleDisable()
        moduleInstance = null
    }
}

interface Module<in T> {
    fun onModuleEnable(information: T) {
        //default
    }

    fun onModuleDisable() {
        //default
    }
}

interface ModuleLifeCycle<T, A : Annotation> {
    fun addModule(annotation: A, module: ModuleWrapper<T>)
    fun getAllModules(): Set<ModuleWrapper<T>>
    fun getAllLoadedModule(): Set<ModuleWrapper<T>>
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Scope

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Disable