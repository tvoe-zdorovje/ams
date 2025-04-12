package by.anatolyloyko.ams.administration.permission.graphql.resolver

import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for permission-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to permission management.
 */
@Controller
class PermissionQueriesResolver(
    private val permissionFinder: PermissionFinder
) {
    /**
     * Resolver for finding all permissions.
     *
     * @return list of all permissions.
     *
     * @see PermissionFinder
     */
    @SchemaMapping(typeName = "PermissionQueries")
    fun permissions() = permissionFinder.findAll()
}
