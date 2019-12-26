package kr.heartpattern.spikot.command

/**
 * Represent simple command.
 */
abstract class Command : AbstractCommand() {
    abstract fun execute()
}