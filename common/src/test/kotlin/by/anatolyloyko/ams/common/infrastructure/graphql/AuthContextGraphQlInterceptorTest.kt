package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.graphql.auth.AuthContextGraphQlInterceptor
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_AUTHENTICATION
import graphql.ExecutionInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.function.BiFunction

class AuthContextGraphQlInterceptorTest : WithAssertions {
    private val interceptor = AuthContextGraphQlInterceptor()

    private val request = mockk<WebGraphQlRequest>(relaxed = true)

    private val chain = mockk<WebGraphQlInterceptor.Chain> {
        every { next(any()) } returns Mono.just(mockk(relaxed = true))
    }

    @Test
    fun `must inject logged user data into graphQL context`() {
        val authenticationToken = UsernamePasswordAuthenticationToken("admin", "admin")
        val securityContext = mockk<SecurityContext> {
            every { authentication } returns authenticationToken
        }
        SecurityContextHolder.setContext(securityContext)

        val monoResult = interceptor.intercept(request, chain)

        StepVerifier.create(monoResult)
            .assertNext {
                val captor = mutableListOf<BiFunction<ExecutionInput, ExecutionInput.Builder, ExecutionInput>>()
                verify {
                    request.configureExecutionInput(capture(captor))
                }

                val executionInputBuilder = ExecutionInput.Builder().query("query")
                val graphQLContext = captor[0]
                    .apply(mockk(), executionInputBuilder)
                    .graphQLContext
                val graphQlContextAuthentication = graphQLContext
                    .get<UsernamePasswordAuthenticationToken>(CONTEXT_AUTHENTICATION)

                assertThat(graphQlContextAuthentication).isSameAs(authenticationToken)

                verify(exactly = 1) {
                    chain.next(request)
                }
            }
            .verifyComplete()
    }

    @Test
    fun `must set null when security context is empty`() {
        SecurityContextHolder.clearContext()

        val monoResult = interceptor.intercept(request, chain)

        StepVerifier.create(monoResult)
            .assertNext {
                verify(exactly = 0) {
                    request.configureExecutionInput(any())
                }
                verify(exactly = 1) {
                    chain.next(request)
                }
            }
            .verifyComplete()
    }
}
