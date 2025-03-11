package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.query.GetBrandQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for brand-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to brand management.
 * Uses the {@link QueryGateway} to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class BrandQueriesResolver(
    private val queryGateway: QueryGateway
) {
    /**
     * Resolver for finding a brand by ID.
     *
     * @param id the identifier of the brand to retrieve.
     * @return the brand data, or `null` if the brand is not found.
     *
     * @see GetBrandQuery
     */
    @SchemaMapping(typeName = "BrandQueries")
    fun brand(
        @Argument id: Long
    ) = queryGateway.handle(GetBrandQuery(id))
}
