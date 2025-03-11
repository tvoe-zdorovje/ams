package by.anatolyloyko.ams.common.infrastructure.service.command

import by.anatolyloyko.ams.common.infrastructure.logging.log
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * Implementation of {@link CommandGateway}.
 *
 * This implementation maintains a cache of command handlers and ensures
 * that each command is dispatched to the appropriate handler for execution.
 *
 * @param commandHandlers the collection of available command handlers.
 *
 * @see Command
 * @see CommandHandler
 */
@Service
class CommandGatewayImpl(
    private val commandHandlers: Collection<CommandHandler>
) : CommandGateway {
    private val commandHandlersMap = ConcurrentHashMap<Class<Command<*>>, CommandHandler>()

    /**
     * Dispatches the given command to the appropriate handler and returns the result.
     *
     * The handler is determined dynamically and cached for future calls.
     *
     * @param R the command execution result.
     * @param command the command to handle.
     * @return the command execution result.
     * @throws IllegalStateException if no suitable handler is found.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <R> handle(command: Command<R>): R = findCommandHandler(command).handle(command) as R

    /**
     * Finds a suitable handler for the given command.
     *
     * If no handler is found, an error is logged and an exception is thrown.
     *
     * @param command the command to find a handler for.
     * @return the command handler capable of processing the command.
     * @throws IllegalStateException if no handler is found.
     *
     * @see CommandHandler.canHandle
     */
    private fun findCommandHandler(command: Command<*>) = try {
        commandHandlersMap.computeIfAbsent(command.javaClass) {
            commandHandlers.single { it.canHandle(command) }
        }
    } catch (e: Exception) {
        val message = "Cannot find command handler for $command"
        log.error(message, e)
        throw IllegalStateException(message, e)
    }
}
