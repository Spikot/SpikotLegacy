package io.github.ReadyMadeProgrammer

import org.junit.Test
import org.reflections.Reflections

class HelloTest: DummyClass(){
    val a:Int = 3
    companion object: DummyClass(){
        val b:Int = 2
    }
}

open class DummyClass

class TestCase{
    @Test fun reflections(){
        Reflections().getSubTypesOf(DummyClass::class.java).forEach{println(it)}
    }
}