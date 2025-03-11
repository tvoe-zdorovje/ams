package by.anatolyloyko.ams.common.infrastructure.service.command

import by.anatolyloyko.ams.common.infrastructure.logging.log
import java.lang.reflect.ParameterizedType

/**
 * A base class for command handlers that process specific command types.
 *
 * @param I the type of command handled by this handler.
 * @param R the command execution result type.
 */
abstract class BaseCommandHandler<I : Command<R>, R : Any> : CommandHandler {
    private val commandType = checkNotNull(
        (this.javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments
            ?.get(0)
    ) { "Cannot detect command class" }

    /**
     * Checks if this handler can process the given command by comparing
     * the type of the provided command with the parameterized type {@code I}.
     *
     * @param command the command to check.
     * @return `true` if the handler can process the command, `false` otherwise.
     */
    override fun canHandle(command: Command<*>): Boolean = commandType == command.javaClass

    /**
     * Processes the given command and returns the execution result.
     *
     * Logs the command execution process before and after calling {@link #handleInternal}.
     *
     * @param command the command to handle.
     * @return the result of the command execution.
     */
    override fun handle(command: Command<*>): R {
        log.info("Handling command $command")
        @Suppress("UNCHECKED_CAST")
        val result = handleInternal(command as I)
        log.info("Handling command $command completed.\nResult: $result")

        return result
    }

    /**
     * Executes the command-specific logic.
     *
     * @param command the command to process.
     * @return the command execution result.
     */
    protected abstract fun handleInternal(command: I): R
}
