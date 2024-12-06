package by.anatolyloyko.ams.infrastructure.service.command

/**
 * Represents a command that can be executed by command handlers producing a result.
 *
 * @see CommandHandler
 * @see CommandGateway
 * @param R the type of command execution result.
 */
interface Command<R>
