package by.anatolyloyko.ams.auth.token.command

import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput

/**
 * A command intended for generation access tokens.
 *
 * @param input contains necessary user data.
 */
data class GenerateTokenCommand(
    val input: GenerateTokenCommandInput,
)
