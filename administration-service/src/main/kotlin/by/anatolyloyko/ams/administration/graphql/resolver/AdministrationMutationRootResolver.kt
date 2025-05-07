package by.anatolyloyko.ams.administration.graphql.resolver

import by.anatolyloyko.ams.administration.brand.graphql.resolver.BrandMutationsResolver
import by.anatolyloyko.ams.administration.brand.studio.graphql.resolver.StudioMutationsResolver
import by.anatolyloyko.ams.administration.role.graphql.resolver.RoleMutationsResolver
import by.anatolyloyko.ams.administration.user.graphql.resolver.UserMutationsResolver
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
    private val brandMutationsResolver: BrandMutationsResolver,
    private val studioMutationsResolver: StudioMutationsResolver,
    private val userMutationsResolver: UserMutationsResolver
) {
    @MutationMapping
    fun roles() = roleMutationsResolver

    @MutationMapping
    fun brands() = brandMutationsResolver

    @MutationMapping
    fun studios() = studioMutationsResolver

    @MutationMapping
    fun users() = userMutationsResolver
}
