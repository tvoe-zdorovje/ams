package by.anatolyloyko.ams.auth.token.command

/**
 * Handles Token-related commands.
 */
interface TokenCommandHandler {
    /**
     * Handles {@link GenerateTokenCommand}.
     */
    fun handle(command: GenerateTokenCommand): String
}
