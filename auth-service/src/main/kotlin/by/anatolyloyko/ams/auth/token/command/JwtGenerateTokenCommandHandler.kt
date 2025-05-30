package by.anatolyloyko.ams.auth.token.command

import by.anatolyloyko.ams.auth.token.action.GenerateTokenAction
import by.anatolyloyko.ams.auth.token.finder.TokenDataFinder
import org.springframework.stereotype.Component

@Component
class JwtGenerateTokenCommandHandler(
    private val tokenDataFinder: TokenDataFinder,
    private val generateTokenAction: GenerateTokenAction
) : TokenCommandHandler {
    override fun handle(command: GenerateTokenCommand): String = generateTokenAction(
        tokenDataFinder.findByUserId(command.input.userId)
    )
}
