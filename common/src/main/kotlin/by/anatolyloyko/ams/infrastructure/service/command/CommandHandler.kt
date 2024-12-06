package by.anatolyloyko.ams.infrastructure.service.command

/**
 * Defines a handler for executing commands.
 *
 * Implementations of this interface process specific commands and produce results.
 *
 * @see Command
 */
interface CommandHandler {
    /**
     * Determines whether this handler can process the given command.
     *
     * @param command the command to check.
     * @return `true` if the handler can process the command, `false` otherwise.
     */
    fun canHandle(command: Command<*>): Boolean

    /**
     * Processes the given command and returns the execution result.
     *
     * @param command the command to handle.
     * @return the command execution result.
     */
    fun handle(command: Command<*>): Any
}
