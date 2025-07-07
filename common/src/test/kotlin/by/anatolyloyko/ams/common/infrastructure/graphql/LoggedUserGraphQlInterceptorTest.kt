package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.model.LoggedUser
import graphql.ExecutionInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import java.util.function.BiFunction

private const val USER_ID = 100000001413121100

class LoggedUserGraphQlInterceptorTest : WithAssertions {
    private val interceptor = LoggedUserGraphQlInterceptor()

    private val request = mockk<WebGraphQlRequest>(relaxed = true)

    private val chain = mockk<WebGraphQlInterceptor.Chain>(relaxed = true)

    @Test
    fun `must inject logged user instance into graphQL context`() {
        every { request.headers[HEADER_USER_ID] } returns listOf(USER_ID.toString())

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
        val actualLoggedUser = graphQLContext.get<LoggedUser>(ARGUMENT_LOGGED_USER)
        assertThat(actualLoggedUser).isEqualTo(LoggedUser(USER_ID))
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
}
