package by.anatolyloyko.ams.administration.role.graphql.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for role-related mutations.
 *
 * This resolver serves as the entry point for role mutations in the GraphQL API.
 * It delegates the mutation handling to the {@link RoleMutationsResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for role mutations in the GraphQL API.
 *
 * @see RoleMutationsResolver
 */

@Controller
class RoleMutationRootResolver(
    private val roleMutationsResolver: RoleMutationsResolver
) {
    @MutationMapping
    fun roles() = roleMutationsResolver
}
