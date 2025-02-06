package by.anatolyloyko.ams.brand.graphql.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for brand-related queries.
 *
 * This resolver serves as the entry point for brand queries in the GraphQL API.
 * It delegates the query handling to the {@link BrandQueriesResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 *
 * @see BrandQueriesResolver
 */
@Controller
class BrandQueryRootResolver(
    private val brandQueriesResolver: BrandQueriesResolver
) {
    @QueryMapping
    fun brands() = brandQueriesResolver
}
