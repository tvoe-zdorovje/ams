package by.anatolyloyko.ams.common.infrastructure.model

/**
 * Represents the authenticated user performing a request.
 *
 * This object is used to pass user-related information, such as the user ID,
 * through the GraphQL context and into resolvers. It enables domain logic
 * to be executed on behalf of the authenticated user.
 *
 * @see LoggedUserGraphQlInterceptor
 * @see LoggedUserArgumentResolver
 */
data class LoggedUser(
    val id: Long
)
