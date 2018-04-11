package io.github.ReadyMadeProgrammer.Spikot

import kotlin.reflect.KClass

fun getScopedAnnotation(clazz: KClass<*>): Set<Annotation>{
    val set = mutableSetOf<Annotation>()
    clazz.annotations.forEach{
        if(it.annotationClass.annotations.any{it.annotationClass==Scope::annotationClass}){
            set.addAll(getScopedAnnotation(it))
        }
    }
    return set
}

fun getScopedAnnotation(annotation: Annotation): Set<Annotation>{
    val set = mutableSetOf<Annotation>()
    set.add(annotation)
    annotation.annotationClass.annotations.forEach{
        val anno = it.annotationClass.annotations.find{it.annotationClass==Scope::annotationClass}
        if(anno!=null){
            set.addAll(getScopedAnnotation(anno::class))
        }
    }
    return set
}
val KClass<out Annotation>.scoped: Boolean
    get() = this.annotations.any{ it.annotationClass.simpleName==Scope::class.simpleName}
val KClass<out Module<*>>.scope: Annotation?
    get() = this.annotations.find{ it.annotationClass.scoped}
val KClass<out Module<*>>.disabled: Boolean
    get() = this.annotations.any{it.annotationClass.simpleName==Disable::class.simpleName}