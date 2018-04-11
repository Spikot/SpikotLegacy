package io.github.ReadyMadeProgrammer

import io.github.ReadyMadeProgrammer.Spikot.*
import org.junit.Test

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Scope
annotation class TestScope

class TestContext(val error: Boolean)

@TestScope
class TestModule(): Module<TestContext> {
    override fun onModuleEnable(information: TestContext) {
        if(information.error)throw AssertionError()
        println("Enable")
    }

    override fun onModuleDisable() {
        println("Disable")
    }
}

object TestLifeCycle: ModuleLifeCycle<TestContext>{
    private val map = HashSet<ModuleWrapper<TestContext>>()
    override fun addModule(annotation: Annotation, module: ModuleWrapper<TestContext>) {
        map.add(module)
    }

    override fun getAllModules(): Set<ModuleWrapper<TestContext>> {
        return map
    }

    override fun getAllLoadedModule(): Set<ModuleWrapper<TestContext>> {
        return map
    }
    fun enable(){
        map.forEach{it.enable(TestContext(false))}
    }
    fun disable(){
        map.forEach{it.disable()}
    }
}

class ModuleTest{
    @Test fun test(){
        ModuleManager.load()
        ModuleManager.addModuleLifeCycle(TestScope::class,TestLifeCycle)
        TestLifeCycle.enable()
        TestLifeCycle.disable()
    }
}