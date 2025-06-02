package by.anatolyloyko.ams.auth.graphql.resolver

import by.anatolyloyko.ams.auth.action.AuthorizeUserAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

private const val USER_ID = 100000001413121100

private const val PHONE_NUMBER = "+375297671245"

private const val PASSWORD = "strong_password"

private const val ACCESS_TOKEN = "my-awesome-access-token"

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class AuthMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var authorizeUserAction: AuthorizeUserAction

    @MockkBean
    lateinit var tokenCommandHandler: TokenCommandHandler

    @Nested
    inner class Login {
        @Test
        fun `must authorize user and return access token`() {
            every { authorizeUserAction(any(), any()) } returns USER_ID
            every { tokenCommandHandler.handle(any<GenerateTokenCommand>()) } returns ACCESS_TOKEN

            val result = graphQlTester
                .documentName("auth/login")
                .variable("phoneNumber", PHONE_NUMBER)
                .variable("password", PASSWORD)
                .execute()

            result.errors().verify()
            result.path("auth.login.userId").entity(Long::class.java).isEqualTo(USER_ID)
            result.path("auth.login.accessToken").entity(String::class.java).isEqualTo(ACCESS_TOKEN)

            verify(exactly = 1) {
                authorizeUserAction(PHONE_NUMBER, PASSWORD.toCharArray())
                tokenCommandHandler.handle(
                    GenerateTokenCommand(
                        input = GenerateTokenCommandInput(USER_ID)
                    )
                )
            }
        }
    }
}
