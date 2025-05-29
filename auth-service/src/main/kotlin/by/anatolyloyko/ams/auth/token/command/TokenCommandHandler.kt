package by.anatolyloyko.ams.auth.token.command

/**
 * Handles Token-related commands.
 */
interface TokenCommandHandler {
    fun handle(command: GenerateTokenCommand): String
}
