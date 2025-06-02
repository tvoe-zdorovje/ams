package by.anatolyloyko.ams.auth.token.jwt.command

import by.anatolyloyko.ams.auth.token.action.GenerateTokenAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import by.anatolyloyko.ams.auth.token.finder.TokenDataFinder
import by.anatolyloyko.ams.auth.token.model.TokenData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

private const val USER_ID = 100000001413121100

private const val JWT = "my-awesome-jwt"

class JwtGenerateTokenCommandHandlerTest : WithAssertions {
    private val tokenData = TokenData(
        userId = USER_ID,
        permissions = emptyMap()
    )

    private val tokenDataFinder = mockk<TokenDataFinder> {
        every { findByUserId(USER_ID) } returns tokenData
    }

    private val generateTokenAction = mockk<GenerateTokenAction> {
        every { this@mockk(tokenData) } returns JWT
    }

    private val commandHandler = JwtGenerateTokenCommandHandler(tokenDataFinder, generateTokenAction)

    private val command = GenerateTokenCommand(GenerateTokenCommandInput(USER_ID))

    @Test
    fun `must find token data by user ID`() {
        commandHandler.handle(command)

        verify { tokenDataFinder.findByUserId(USER_ID) }
    }

    @Test
    fun `must call generateTokenAction with valid token data`() {
        commandHandler.handle(command)

        verify { generateTokenAction(tokenData) }
    }

    @Test
    fun `must return generated JWT`() {
        val result = commandHandler.handle(command)

        assertThat(result).isEqualTo(JWT)
    }
}
