package kr.heartpattern.spikot.command

abstract class Command : AbstractCommand() {
    abstract fun execute()
}