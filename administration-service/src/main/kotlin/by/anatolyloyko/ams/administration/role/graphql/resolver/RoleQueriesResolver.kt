package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.administration.role.query.GetRoleQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for role-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to role management.
 * Uses the {@link QueryGateway} to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class RoleQueriesResolver(
    private val queryGateway: QueryGateway,
    private val permissionFinder: PermissionFinder
) {
    /**
     * Resolver for finding a role by ID.
     *
     * @param id the identifier of the role to retrieve.
     * @return the role data, or `null` if the role is not found.
     *
     * @see GetRoleQuery
     */
    @SchemaMapping(typeName = "RoleQueries")
    fun role(
        @Argument id: Long
    ) = queryGateway.handle(GetRoleQuery(id))

    /**
     * Resolver for finding role permissions.
     *
     * @param role the role which permissions should be found for.
     * @return list of permissions related to the role.
     *
     * @see PermissionFinder
     */
    @SchemaMapping(typeName = "Role")
    fun permissions(role: Role) = permissionFinder.findByRoleId(role.id!!)
}
