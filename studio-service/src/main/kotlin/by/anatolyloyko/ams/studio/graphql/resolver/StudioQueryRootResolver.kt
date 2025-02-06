package by.anatolyloyko.ams.studio.graphql.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for studio-related queries.
 *
 * This resolver serves as the entry point for studio queries in the GraphQL API.
 * It delegates the query handling to the {@link StudioQueriesResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 *
 * @see StudioQueriesResolver
 */
@Controller
class StudioQueryRootResolver(
    private val studioQueriesResolver: StudioQueriesResolver
) {
    @QueryMapping
    fun studios() = studioQueriesResolver
}
