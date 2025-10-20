package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.graphql.auth.AuthContextGraphQlInterceptor
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_LOGGED_USER
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_AUTHORIZATION
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_AUTHORIZATION_PREFIX
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.HEADER_USER_ID
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUser
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUserTokenData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.ExecutionInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.graphql.execution.ErrorType.UNAUTHORIZED
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import java.time.Duration
import java.util.Base64
import java.util.function.BiFunction

private const val USER_ID = 100000001413121100

private const val ORGANIZATION_ID = 1000000001513221100

private const val TEST_PERMISSION_NAME = "testPermission"

private const val JWT_PAYLOAD = """
    {
        "data": {
            "userId": $USER_ID,
            "permissions": {
                "$ORGANIZATION_ID": [
                    {
                        "id": -1001,
                        "name": "$TEST_PERMISSION_NAME"
                    }
                ]
            }
        }
    }
"""

class AuthContextGraphQlInterceptorTest : WithAssertions {
    private val interceptor = AuthContextGraphQlInterceptor(jacksonObjectMapper())

    private val request = mockk<WebGraphQlRequest>(relaxed = true)

    private val chain = mockk<WebGraphQlInterceptor.Chain>(relaxed = true)

    @Test
    fun `must inject logged user data into graphQL context`() {
        every { request.headers[HEADER_USER_ID] } returns listOf(USER_ID.toString())
        val mockJwt = "header.${Base64.getEncoder().encodeToString(JWT_PAYLOAD.toByteArray())}.signature"
        every { request.headers[HEADER_AUTHORIZATION] } returns listOf("$HEADER_AUTHORIZATION_PREFIX$mockJwt")

        interceptor.intercept(request, chain)

        val configurerCaptor = mutableListOf<BiFunction<ExecutionInput, ExecutionInput.Builder, ExecutionInput>>()
        verifyOrder {
            request.configureExecutionInput(capture(configurerCaptor))

            chain.next(request)
        }

        val executionInputBuilder = ExecutionInput.Builder().query("query")
        val graphQLContext = configurerCaptor[0]
            .apply(mockk(), executionInputBuilder)
            .graphQLContext

        val actualLoggedUser = graphQLContext.get<LoggedUser>(CONTEXT_LOGGED_USER)
        assertThat(actualLoggedUser).isEqualTo(LoggedUser(USER_ID))

        val actualLoggedUserTokenData = graphQLContext.get<LoggedUserTokenData>(CONTEXT_LOGGED_USER)
        assertThat(actualLoggedUserTokenData.id).isEqualTo(USER_ID)
        assertThat(actualLoggedUserTokenData.permissions).hasSize(1)
        assertThat(actualLoggedUserTokenData.permissions).containsKey(ORGANIZATION_ID)
        assertThat(actualLoggedUserTokenData.permissions[ORGANIZATION_ID]).containsExactly(TEST_PERMISSION_NAME)
    }

    @Test
    fun `must skip logic when header value is null`() {
        every { request.headers[HEADER_USER_ID] } returns null

        interceptor.intercept(request, chain)

        verify(exactly = 0) {
            request.configureExecutionInput(any())
        }
        verify(exactly = 1) {
            chain.next(request)
        }
    }

    @Test
    fun `must return error when authorization header is null`() {
        every { request.headers[HEADER_USER_ID] } returns listOf(USER_ID.toString())
        every { request.headers[HEADER_AUTHORIZATION] } returns null

        val result = interceptor.intercept(request, chain).block(Duration.ZERO)

        val error = result.errors.first()
        assertThat(error.errorType).isEqualTo(UNAUTHORIZED)
        assertThat(error.message).isEqualTo("Failed to parse authorization token data.")

        verify(exactly = 0) {
            request.configureExecutionInput(any())
        }
        verify(exactly = 0) {
            chain.next(request)
        }
    }

    @Test
    fun `must return error when cannot parse token data`() {
        every { request.headers[HEADER_USER_ID] } returns listOf(USER_ID.toString())
        val invalidJwtPayload = "invalid"
        val mockJwt = "header.${Base64.getEncoder().encodeToString(invalidJwtPayload.toByteArray())}.signature"
        every { request.headers[HEADER_AUTHORIZATION] } returns listOf("$HEADER_AUTHORIZATION_PREFIX$mockJwt")

        val result = interceptor.intercept(request, chain).block(Duration.ZERO)

        val error = result.errors.first()
        assertThat(error.errorType).isEqualTo(UNAUTHORIZED)
        assertThat(error.message).isEqualTo("Failed to parse authorization token data.")

        verify(exactly = 0) {
            request.configureExecutionInput(any())
        }
        verify(exactly = 0) {
            chain.next(request)
        }
    }
}
