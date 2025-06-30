package by.anatolyloyko.ams.auth.token.jwt.command

import by.anatolyloyko.ams.auth.token.action.GenerateTokenAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.finder.TokenDataFinder
import org.springframework.stereotype.Component

/**
 * {@inheritDoc}
 *
 * Implementation for working with JWT.
 */
@Component
class JwtGenerateTokenCommandHandler(
    private val tokenDataFinder: TokenDataFinder,
    private val generateTokenAction: GenerateTokenAction
) : TokenCommandHandler {
    /**
     * {@inheritDoc}
     */
    override fun handle(command: GenerateTokenCommand): String = generateTokenAction(
        tokenDataFinder.findByUserId(command.input.userId)
    )
}
