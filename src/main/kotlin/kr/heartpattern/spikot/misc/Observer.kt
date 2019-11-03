@file:Suppress("unused")

package kr.heartpattern.spikot.misc

const val ACCEPT = true
const val DENY = false

typealias Observer<T> = (T) -> Unit
typealias VetoableObserver<T> = (T) -> Boolean
typealias TransformObserver<T, U> = (T, U) -> Unit
typealias TransformVetoableObserver<T, U> = (T, U) -> Boolean