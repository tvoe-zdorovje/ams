package by.anatolyloyko.ams.studio.graphql.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for studio-related mutations.
 *
 * This resolver serves as the entry point for studio mutations in the GraphQL API.
 * It delegates the mutation handling to the {@link StudioMutationsResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for studio mutations in the GraphQL API.
 *
 * @see StudioMutationsResolver
 */

@Controller
class StudioMutationRootResolver(
    private val studioMutationsResolver: StudioMutationsResolver
) {
    @MutationMapping
    fun studios() = studioMutationsResolver
}
