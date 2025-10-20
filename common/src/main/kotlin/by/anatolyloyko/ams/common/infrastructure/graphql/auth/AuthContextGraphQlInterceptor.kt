package by.anatolyloyko.ams.common.infrastructure.graphql.auth

import by.anatolyloyko.ams.common.infrastructure.exception.AuthorizationException
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUser
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUserTokenData
import by.anatolyloyko.ams.common.infrastructure.logging.log
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.ExecutionResultImpl
import graphql.GraphqlErrorBuilder
import org.springframework.graphql.execution.ErrorType.UNAUTHORIZED
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.Base64

internal const val CONTEXT_LOGGED_USER = "loggedUser"

internal const val HEADER_USER_ID = "user-id"

internal const val HEADER_AUTHORIZATION = "Authorization"

internal const val HEADER_AUTHORIZATION_PREFIX = "Bearer "

internal const val JWT_TOKEN_PARAMETER_NAME_DATA = "data"

/**
 * GraphQL interceptor that extracts authentication data from HTTP headers
 * and injects [LoggedUser] and [LoggedUserTokenData] instance into the GraphQL context.
 *
 * This allows resolvers to access the authenticated user via a custom argument resolvers
 * such as [LoggedUserArgumentResolver], typically marked with the [Principal] annotation.
 */
@Component
class AuthContextGraphQlInterceptor(
    private val objectMapper: ObjectMapper
) : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse> = try {
        request
            .headers[HEADER_USER_ID]
            ?.firstNotNullOfOrNull { LoggedUser(id = it.toLong()) }
            ?.also { loggedUser ->
                val userWithTokenData = extractAccessTokenData(request.headers[HEADER_AUTHORIZATION])
                assert(loggedUser.id == userWithTokenData.id)

                request.configureExecutionInput { _, builder ->
                    builder
                        .graphQLContext { it.put(CONTEXT_LOGGED_USER, userWithTokenData) }
                        .build()
                }
            }

        chain.next(request)
    } catch (e: Exception) {
        val message = if (e is AuthorizationException) e.localizedMessage else "Authorization failed: ${e.message}"
        log.warn(message, e)

        Mono.just(
            WebGraphQlResponse(
                DefaultExecutionGraphQlResponse(
                    request.toExecutionInput(),
                    ExecutionResultImpl
                        .newExecutionResult()
                        .addError(
                            GraphqlErrorBuilder
                                .newError()
                                .errorType(UNAUTHORIZED)
                                .message(message)
                                .build()
                        )
                        .build()
                )
            )
        )
    }

    private fun extractAccessTokenData(authorizationHeaderValue: List<String>?): LoggedUserTokenData = try {
        authorizationHeaderValue
            ?.firstNotNullOfOrNull { it.removePrefix(HEADER_AUTHORIZATION_PREFIX) }
            ?.let { parseJWT(it) }
            ?: throw AuthorizationException("Authorization token value is invalid.")
    } catch (ex: Exception) {
        throw AuthorizationException("Failed to parse authorization token data.", ex)
    }

    private fun parseJWT(jwt: String): LoggedUserTokenData {
        val jwtParts = jwt.split(".")
        @Suppress("MagicNumber")
        check(jwtParts.size == 3) { "JWT is invalid." }
        val payloadJson = String(Base64.getUrlDecoder().decode(jwtParts[1]))
        val tokenDataNode = objectMapper.readTree(payloadJson)[JWT_TOKEN_PARAMETER_NAME_DATA]

        return objectMapper.treeToValue(tokenDataNode, LoggedUserTokenData::class.java)
    }
}
