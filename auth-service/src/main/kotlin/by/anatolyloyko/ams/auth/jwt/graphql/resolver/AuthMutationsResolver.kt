package by.anatolyloyko.ams.auth.jwt.graphql.resolver

import by.anatolyloyko.ams.auth.jwt.graphql.dto.LoginRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for auth-related mutations in the GraphQL API.
 */
@Controller
class AuthMutationsResolver {
    /**
     * Resolves the login mutation for authorization and receiving a JWT by provided credentials.
     *
     * @param request the request containing an information required for authorization.
     * @return JWT.
     */
    @SchemaMapping(typeName = "AuthMutations")
    fun login(
        @Argument request: LoginRequest,
    ): Long = TODO("Implement me") // data loader?
}