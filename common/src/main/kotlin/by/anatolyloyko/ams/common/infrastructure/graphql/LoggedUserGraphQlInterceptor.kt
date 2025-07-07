package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.model.LoggedUser
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

internal const val ARGUMENT_LOGGED_USER = "loggedUser"

internal const val HEADER_USER_ID = "user-id"

/**
 * GraphQL interceptor that extracts authentication data from HTTP headers
 * and injects a [LoggedUser] instance into the GraphQL context.
 *
 * This allows resolvers to access the authenticated user via a custom argument resolvers
 * such as [LoggedUserArgumentResolver], typically marked with the [Principal] annotation.
 */
@Component
class LoggedUserGraphQlInterceptor : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        request
            .headers[HEADER_USER_ID]
            ?.firstNotNullOfOrNull { LoggedUser(id = it.toLong()) }
            ?.also { loggedUser ->
                request.configureExecutionInput { _, builder ->
                    builder
                        .graphQLContext { it.put(ARGUMENT_LOGGED_USER, loggedUser) }
                        .build()
                }
            }

        return chain.next(request)
    }
}
