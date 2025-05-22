package by.anatolyloyko.ams.auth.graphql.resolver

import by.anatolyloyko.ams.auth.jwt.graphql.resolver.AuthMutationsResolver
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for auth-related mutations.
 *
 * This resolver serves as the entry point for auth-related mutations in the GraphQL API.
 * It delegates the mutation handling to domain-specific resolvers,
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for auth-related mutations in the GraphQL API.
 */

@Controller
class AuthMutationRootResolver(
    private val authMutationsResolver: AuthMutationsResolver
) {
    @MutationMapping
    fun auth() = authMutationsResolver
}
