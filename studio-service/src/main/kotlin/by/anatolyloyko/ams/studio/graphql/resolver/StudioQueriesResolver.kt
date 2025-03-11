package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.studio.query.GetStudioQuery
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for studio-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to studio management.
 * Uses the {@link QueryGateway} to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class StudioQueriesResolver(
    private val queryGateway: QueryGateway
) {
    /**
     * Resolver for finding a studio by ID.
     *
     * @param id the identifier of the studio to retrieve.
     * @return the studio data, or `null` if the studio is not found.
     *
     * @see GetStudioQuery
     */
    @SchemaMapping(typeName = "StudioQueries")
    fun studio(
        @Argument id: Long
    ) = queryGateway.handle(GetStudioQuery(id))
}
