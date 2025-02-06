package by.anatolyloyko.ams.user.graphql.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for user-related queries.
 *
 * This resolver serves as the entry point for user queries in the GraphQL API.
 * It delegates the query handling to the {@link UserQueriesResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 *
 * @see UserQueriesResolver
 */
@Controller
class UserQueryRootResolver(
    private val userQueriesResolver: UserQueriesResolver
) {
    @QueryMapping
    fun users() = userQueriesResolver
}
