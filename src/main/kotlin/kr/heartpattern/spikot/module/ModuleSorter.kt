package kr.heartpattern.spikot.module

import com.google.common.collect.HashMultimap
import kr.heartpattern.spikot.spikot
import java.util.*
import java.util.concurrent.atomic.LongAdder
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation


private object BeginModule : AbstractModule()
private object EndModule : AbstractModule()

internal fun sortModuleDependencies(modules: List<KClass<*>>): List<KClass<*>> {
    val dependencies = HashMultimap.create<KClass<*>, KClass<*>>()
    val counting = HashMap<KClass<*>, LongAdder>()
    for (module in modules) {
        counting.computeIfAbsent(module){LongAdder()}
        val before = module.findAnnotation<Module>()!!.depend
        val after = module.findAnnotation<LoadBefore>()?.value ?: arrayOf()

        var loadFirst = false
        var loadLast = false

        for (element in after) {
            if (element == IModule::class) {
                loadFirst = true
                dependencies.put(module, BeginModule::class)
                counting.getOrPut(BeginModule::class, ::LongAdder).increment()
            } else {
                dependencies.put(module, element)
                counting.getOrPut(element, ::LongAdder).increment()
            }
        }

        for (element in before) {
            if (element == IModule::class) {
                loadLast = true
                dependencies.put(EndModule::class, module)
                counting.getOrPut(module, ::LongAdder).increment()
            } else {
                dependencies.put(element, module)
                counting.getOrPut(module, ::LongAdder).increment()
            }
        }

        if (!loadFirst) {
            dependencies.put(BeginModule::class, module)
            counting.getOrPut(module, ::LongAdder).increment()
        }

        if (!loadLast) {
            dependencies.put(module, EndModule::class)
            counting.getOrPut(EndModule::class, ::LongAdder).increment()
        }
    }

    val zeros = counting.asSequence()
        .filter { it.value.sum() == 0L }
        .map { it.key }
        .toList()

    val result = LinkedList<KClass<*>>()

    for (start in zeros) {
        val queue = LinkedList<KClass<*>>()
        queue += start

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result += current

            for (child in dependencies[current]) {
                val count = counting[child]!!
                count.decrement()
                if (count.sum() == 0L)
                    queue += child
            }
        }
    }

    if(result.size != modules.size + 2) {
        val diff = modules.filterNot{result.contains(it)}
        spikot.logger.error("Circular module dependency found. Following module produce conflict")
        for(module in diff){
            spikot.logger.error(module.qualifiedName)
        }
        throw IllegalStateException("Circular module dependency found")
    }

    return result.filter { it != BeginModule::class && it != EndModule::class }
}