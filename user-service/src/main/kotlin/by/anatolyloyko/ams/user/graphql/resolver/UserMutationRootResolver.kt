package by.anatolyloyko.ams.user.graphql.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for user-related mutations.
 *
 * This resolver serves as the entry point for user mutations in the GraphQL API.
 * It delegates the mutation handling to the {@link UserMutationsResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for user mutations in the GraphQL API.
 *
 * @see UserMutationsResolver
 */

@Controller
class UserMutationRootResolver(
    private val userMutationsResolver: UserMutationsResolver
) {
    @MutationMapping
    fun users() = userMutationsResolver
}
