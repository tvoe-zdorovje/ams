package by.anatolyloyko.ams.auth.graphql.resolver

import by.anatolyloyko.ams.auth.action.AuthorizeUserAction
import by.anatolyloyko.ams.auth.graphql.dto.LoginRequest
import by.anatolyloyko.ams.auth.graphql.dto.LoginResponse
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for auth-related mutations in the GraphQL API.
 */
@Controller
class AuthMutationsResolver(
    private val tokenCommandHandler: TokenCommandHandler,
    private val authorizeUserAction: AuthorizeUserAction
) {
    /**
     * Resolves the login mutation for authorization and receiving a JWT by provided credentials.
     *
     * @param request the request containing an information required for authorization.
     * @return JWT.
     */
    @SchemaMapping(typeName = "AuthMutations")
    fun login(
        @Argument request: LoginRequest,
    ): LoginResponse {
        val userId = authorizeUserAction(
            phoneNumber = request.phoneNumber,
            password = request.password
        )

        return LoginResponse(
            userId = userId,
            accessToken = tokenCommandHandler.handle(
                GenerateTokenCommand(
                    input = GenerateTokenCommandInput(userId)
                )
            )
        )
    }
}
