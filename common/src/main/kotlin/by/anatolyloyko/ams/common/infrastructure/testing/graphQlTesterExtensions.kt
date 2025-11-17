package by.anatolyloyko.ams.common.infrastructure.testing

import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_AUTHORIZATION
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_AUTHORIZATION_PREFIX
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_USER_ID
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUserTokenData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.graphql.execution.ErrorType.FORBIDDEN
import org.springframework.graphql.execution.ErrorType.UNAUTHORIZED
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.graphql.test.tester.WebGraphQlTester
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Provides a more convenient way to access a specific path in the GraphQL response.
 *
 * This extension function allows accessing a GraphQL response path using the `[]` operator,
 * making it more intuitive when working with `WebGraphQlTester`.
 *
 * Example usage:
 * ```
 * val result = graphQlTester
 *             .documentName("user/getUser")
 *             .variable("id", 1001L)
 *             .execute()
 * val userFirstNamePath = result["\$.data.users.user.firstName"]
 * ```
 *
 * @receiver The `Traversable` instance, representing a navigable part of the GraphQL response.
 * @param path The dot-separated GraphQL path to retrieve.
 * @return A `GraphQlTester.Path` instance for further assertions or extraction.
 */
operator fun GraphQlTester.Traversable.get(path: String): GraphQlTester.Path = path(path)

/**
 * Asserts that the GraphQL response at the given path matches the expected value.
 *
 * This extension function simplifies value verification in GraphQL responses by allowing
 * the use of the `matches` infix function.
 *
 * Example usage:
 * ```
 * val result = graphQlTester
 *             .documentName("user/getUser")
 *             .variable("id", 1001L)
 *             .execute()
 * result["\$.data.users.user.firstName"] matches "Alexey"
 * ```
 *
 * @receiver The `GraphQlTester.Path` representing the value to be verified.
 * @param expected The expected value to compare against.
 * @throws AssertionError if the actual value does not match the expected value.
 */
infix fun <T> GraphQlTester.Path.matches(expected: T) {
    entity(String::class.java).isEqualTo(expected.toString())
}

/**
 * Provides a more convenient way to specify authentication headers.
 *
 * Example usage:
 * ```
 * val result = graphQlTester
 *             .loginAs(USER_ID)
 *             .documentName("user/updateUser")
 *             .variable("firstName", "Alexey")
 *             .variable("lastName", "Kasimov")
 *             .variable("phoneNumber", "+375297671245")
 *             .execute()
 *
 * val result = graphQlTester
 *  *             .loginAs(USER_ID, setOf("createBrand"))
 *  *             .documentName("brand/createBrand")
 *  *             .variable("name", "Lidskoe")
 *  *             .variable("description", "Brew beer")
 *  *             .execute()
 * ```
 *
 * @param userId value for {@code HEADER_USER_ID}
 * @param permissions map of organization ID [Long] to collection of permissions
 * @return WebGraphQlTester instance
 */
fun WebGraphQlTester.loginAs(
    userId: Long,
    permissions: Map<Long, Collection<String>> = emptyMap()
): WebGraphQlTester = mutate()
    .headers {
        it[HEADER_USER_ID] = "$userId"
        it[HEADER_AUTHORIZATION] = "$HEADER_AUTHORIZATION_PREFIX${mockJWT(userId, permissions)}"
    }
    .build()

fun WebGraphQlTester.loginAs(
    userId: Long,
    organizationId: Long = -1,
    vararg permissions: String
) = loginAs(
    userId = userId,
    permissions = mapOf(organizationId to permissions.asList())
)

@OptIn(ExperimentalEncodingApi::class)
private fun mockJWT(userId: Long, permissions: Map<Long, Collection<String>>): String {
    val permissionsJson = jacksonObjectMapper().writeValueAsString(
        permissions.mapValues { entry ->
            entry.value.map { LoggedUserTokenData.Permission(-1, it) }
        }
    )
    val jwtPayload = """ { "data": { "userId": $userId, "permissions": $permissionsJson } } """
    val encodedJwtPayload = Base64.encode(jwtPayload.toByteArray())

    return "headers.${encodedJwtPayload}.signature"
}

/**
 * Expects the [org.springframework.graphql.execution.ErrorType.UNAUTHORIZED] error in the response.
 */
fun GraphQlTester.Response.expectUnauthorized(): GraphQlTester.Errors =
    errors().expect { it.message == "Authorization required" && it.errorType == UNAUTHORIZED }

/**
 * * Expects the [org.springframework.graphql.execution.ErrorType.FORBIDDEN] error in the response.
 */
fun GraphQlTester.Response.expectForbidden(vararg insufficientPermissions: String): GraphQlTester.Errors =
    errors().expect {
        it.message == "Access denied: insufficient permissions - ${insufficientPermissions.asList()}."
            && it.errorType == FORBIDDEN
    }
