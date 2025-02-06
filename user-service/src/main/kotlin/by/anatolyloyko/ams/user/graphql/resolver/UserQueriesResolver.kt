package by.anatolyloyko.ams.user.graphql.resolver

import by.anatolyloyko.ams.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.user.query.GetUserQuery
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for user-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to user management.
 * Uses the {@link QueryGateway} to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class UserQueriesResolver(
    private val queryGateway: QueryGateway
) {
    /**
     * Resolver for finding a user by ID.
     *
     * @param id the identifier of the user to retrieve.
     * @return the user data, or `null` if the user is not found.
     *
     * @see GetUserQuery
     */
    @SchemaMapping(typeName = "UserQueries")
    fun user(
        @Argument id: Long
    ) = queryGateway.handle(GetUserQuery(id))
}
