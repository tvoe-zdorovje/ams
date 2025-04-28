package by.anatolyloyko.ams.administration.role.graphql.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for role-related queries.
 *
 * This resolver serves as the entry point for role queries in the GraphQL API.
 * It delegates the query handling to the {@link RoleQueriesResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 *
 * @see RoleQueriesResolver
 */
@Controller
class RoleQueryRootResolver(
    private val roleQueriesResolver: RoleQueriesResolver
) {
    @QueryMapping
    fun roles() = roleQueriesResolver
}
