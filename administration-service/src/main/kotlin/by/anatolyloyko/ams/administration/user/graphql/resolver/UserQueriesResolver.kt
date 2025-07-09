package by.anatolyloyko.ams.administration.user.graphql.resolver

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.administration.user.graphql.dto.GetUserRolesRequest
import by.anatolyloyko.ams.administration.user.query.GetUserRolesQuery
import by.anatolyloyko.ams.administration.user.query.input.GetUserRolesQueryInput
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for user-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to user management.
 * Uses the [QueryGateway] to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class UserQueriesResolver(
    private val queryGateway: QueryGateway
) {
    /**
     * Resolves the 'roles' query for getting user roles by user ID and organization ID.
     *
     * @param request.userId target user ID.
     * @param request.organizationId either a brand ID or a studio ID that the roles belong to.
     * @return list of [Role]
     *
     * @see GetUserRolesQuery
     */
    @SchemaMapping(typeName = "UserQueries")
    fun roles(
        @Argument request: GetUserRolesRequest
    ): List<Role> = queryGateway.handle(
        GetUserRolesQuery(
            input = GetUserRolesQueryInput(
                userId = request.userId,
                organizationId = request.organizationId
            )
        )
    )
}
