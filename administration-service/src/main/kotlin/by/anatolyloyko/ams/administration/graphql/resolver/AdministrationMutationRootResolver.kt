package by.anatolyloyko.ams.administration.graphql.resolver

import by.anatolyloyko.ams.administration.brand.graphql.resolver.BrandMutationsResolver
import by.anatolyloyko.ams.administration.role.graphql.resolver.RoleMutationsResolver
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for administration-related mutations.
 *
 * This resolver serves as the entry point for administration mutations in the GraphQL API.
 * It delegates the mutation handling to domain-specific resolvers,
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for administration mutations in the GraphQL API.
 */

@Controller
class AdministrationMutationRootResolver(
    private val roleMutationsResolver: RoleMutationsResolver,
    private val brandMutationsResolver: BrandMutationsResolver
) {
    @MutationMapping
    fun roles() = roleMutationsResolver

    @MutationMapping
    fun brandAdm() = brandMutationsResolver
}
