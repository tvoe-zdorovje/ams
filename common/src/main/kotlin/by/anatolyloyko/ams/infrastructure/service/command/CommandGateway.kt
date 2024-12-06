package by.anatolyloyko.ams.infrastructure.service.command

/**
 * A gateway for dispatching commands to their appropriate handlers.
 *
 * The gateway finds a suitable command handler and delegates execution,
 * returning the result produced by the handler.
 *
 * @see Command
 * @see CommandHandler
 */
interface CommandGateway {
    /**
     * Dispatches the given command to an appropriate handler for execution.
     *
     * @param R the command execution result type.
     * @param command the command to handle.
     * @return the command execution result.
     * @throws IllegalStateException if no suitable handler is found.
     */
    fun <R> handle(command: Command<R>): R
}
