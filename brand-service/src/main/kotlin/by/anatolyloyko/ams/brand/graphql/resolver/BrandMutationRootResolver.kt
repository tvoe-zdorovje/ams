package by.anatolyloyko.ams.brand.graphql.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for brand-related mutations.
 *
 * This resolver serves as the entry point for brand mutations in the GraphQL API.
 * It delegates the mutation handling to the {@link BrandMutationsResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for brand mutations in the GraphQL API.
 *
 * @see BrandMutationsResolver
 */

@Controller
class BrandMutationRootResolver(
    private val brandMutationsResolver: BrandMutationsResolver
) {
    @MutationMapping
    fun brands() = brandMutationsResolver
}
