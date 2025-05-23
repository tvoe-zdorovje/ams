package by.anatolyloyko.ams.auth.token.command

import by.anatolyloyko.ams.auth.token.finder.TokenDataFinder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JwtGenerateTokenCommandHandler(
    private val tokenDataFinder: TokenDataFinder
) : TokenCommandHandler {
    override fun handle(command: GenerateTokenCommand): String {
        val data = tokenDataFinder.findByUserId(command.input.userId)

        return ObjectMapper().writeValueAsString(data)
    }
}
