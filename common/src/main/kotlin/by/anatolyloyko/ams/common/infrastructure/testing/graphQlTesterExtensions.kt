package by.anatolyloyko.ams.common.infrastructure.testing

import by.anatolyloyko.ams.common.infrastructure.graphql.HEADER_USER_ID
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.graphql.test.tester.WebGraphQlTester

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
 * ```
 *
 * @param userId value for {@code HEADER_USER_ID}
 * @return WebGraphQlTester instance
 */
infix fun WebGraphQlTester.loginAs(userId: Long): WebGraphQlTester = mutate()
    .headers {
        it[HEADER_USER_ID] = "$userId"
    }
    .build()
