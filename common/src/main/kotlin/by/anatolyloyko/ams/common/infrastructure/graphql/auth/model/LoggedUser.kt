package by.anatolyloyko.ams.common.infrastructure.graphql.auth.model

/**
 * Represents an authenticated user performing a request.
 *
 * This object is used to pass user-related information, such as the user ID, into GraphQL resolvers.
 * It enables actions to be performed on behalf of the authenticated user.
 *
 * @see [by.anatolyloyko.ams.common.infrastructure.graphql.auth.LoggedUserArgumentResolver]
 */
open class LoggedUser(
    val id: Long
)
