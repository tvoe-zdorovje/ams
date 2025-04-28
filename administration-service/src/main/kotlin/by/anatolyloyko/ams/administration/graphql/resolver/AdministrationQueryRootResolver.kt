package by.anatolyloyko.ams.administration.graphql.resolver

import by.anatolyloyko.ams.administration.permission.graphql.resolver.PermissionQueriesResolver
import by.anatolyloyko.ams.administration.role.graphql.resolver.RoleQueriesResolver
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for administration-related queries.
 *
 * This resolver serves as the entry point for administration queries in the GraphQL API.
 * It delegates the query handling to domain-specific resolvers,
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 */
@Controller
class AdministrationQueryRootResolver(
    private val roleQueriesResolver: RoleQueriesResolver,
    private val permissionQueriesResolver: PermissionQueriesResolver
) {
    @QueryMapping
    fun roles() = roleQueriesResolver

    @QueryMapping
    fun permissions() = permissionQueriesResolver
}
