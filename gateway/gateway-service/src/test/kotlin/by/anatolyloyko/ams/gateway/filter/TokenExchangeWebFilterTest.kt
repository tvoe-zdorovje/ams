package by.anatolyloyko.ams.gateway.filter

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.context.Context

private const val URI_TEMPLATE = "/some/uri"

private const val AUTHENTICATION_NAME = "sub"

private const val CREDENTIALS = "password"

private const val TOKEN = "my.awesome.jwt"

private const val REDIS_KEY_PREFIX = "prefix:"

class TokenExchangeWebFilterTest : WithAssertions {
    private val redisTemplate: ReactiveStringRedisTemplate = mockk()

    private val valueOps: ReactiveValueOperations<String, String> = mockk()

    private val chain: WebFilterChain = mockk()

    private val filter: TokenExchangeWebFilter = TokenExchangeWebFilter(redisTemplate, REDIS_KEY_PREFIX)

    private val exchange = MockServerWebExchange.from(
        MockServerHttpRequest
            .get(URI_TEMPLATE)
            .build()
    )

    @BeforeEach
    fun beforeEach() {
        every { redisTemplate.opsForValue() } returns valueOps
        every { valueOps.get(neq("$REDIS_KEY_PREFIX$AUTHENTICATION_NAME")) } returns Mono.empty()
        every { valueOps.get("$REDIS_KEY_PREFIX$AUTHENTICATION_NAME") } returns Mono.just(TOKEN)
        every { chain.filter(any<ServerWebExchange>()) } returns Mono.empty()
    }

    @Test
    fun `must successfully set Bearer token when authentication name and token exist`() {
        val result = filter.filter(exchange, chain)
            .contextWrite(withAuthentication())

        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `must return 401 Unauthorized when security context is empty`() {
        val result = filter.filter(exchange, chain)

        StepVerifier.create(result)
            .expectError(ResponseStatusException::class.java)
            .verify()
    }

    @Test
    fun `must return 401 Unauthorized when token not found in redis`() {
        val authName = "unknown"
        val result = filter.filter(exchange, chain)
            .contextWrite(withAuthentication(authName = authName))

        StepVerifier.create(result)
            .expectErrorSatisfies { error -> error.assertUnauthorized("No token found for user $authName") }
            .verify()
    }

    @Test
    fun `must return 401 Unauthorized when authentication name is null`() {
        val result = filter.filter(exchange, chain)
            .contextWrite(withAuthentication(authName = null))

        StepVerifier.create(result)
            .expectErrorSatisfies { error -> error.assertUnauthorized("Unauthenticated") }
            .verify()
    }

    @Test
    fun `must return 401 Unauthorized when security context not exist`() {
        val result = filter.filter(exchange, chain)

        StepVerifier.create(result)
            .expectErrorSatisfies { error -> error.assertUnauthorized("Unauthenticated") }
            .verify()
    }

    @Test
    fun `must propagate error from chain filter`() {
        val testError = RuntimeException("Chain filter error")

        every { chain.filter(any<ServerWebExchange>()) } returns Mono.error(testError)

        val result = filter.filter(exchange, chain)
            .contextWrite(withAuthentication())

        StepVerifier.create(result)
            .expectErrorSatisfies { error -> assertThat(error).isSameAs(testError) }
            .verify()
    }

    private fun withAuthentication(authName: String? = AUTHENTICATION_NAME): Context =
        ReactiveSecurityContextHolder.withAuthentication(
            UsernamePasswordAuthenticationToken(
                authName,
                CREDENTIALS,
                emptyList()
            )
        )

    private fun Throwable.assertUnauthorized(message: String) {
        assertThat(this).isInstanceOf(ResponseStatusException::class.java)
        val statusException = this as ResponseStatusException
        assertThat(statusException.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(statusException.reason).isEqualTo(message)
    }
}
