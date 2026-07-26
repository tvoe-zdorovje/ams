package by.anatolyloyko.ams.gateway.config

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.test.StepVerifier
import java.net.InetSocketAddress

private const val URI_TEMPLATE = "/some/uri"

private const val HOST_ADDRESS = "127.0.0.1"

class RequestRateLimiterConfigTest : WithAssertions {
    private val config = RequestRateLimiterConfig()

    private val keyResolver = config.keyResolver()

    private val httpRequest = MockServerHttpRequest
        .post(URI_TEMPLATE)
        .remoteAddress(InetSocketAddress(HOST_ADDRESS, 9042))
        .build()

    @Test
    fun `must return principal name`() {
        val principalName = "principalName"
        val monoResult = keyResolver
            .resolve(MockServerWebExchange.from(httpRequest))
            .contextWrite(
                ReactiveSecurityContextHolder.withAuthentication(
                    UsernamePasswordAuthenticationToken(
                        principalName,
                        "password",
                        emptyList()
                    )
                )
            )

        StepVerifier.create(monoResult)
            .assertNext { assertThat(it).isEqualTo(principalName) }
            .verifyComplete()
    }

    @Test
    fun `must return host address when principal absent`() {
        val monoResult = keyResolver
            .resolve(MockServerWebExchange.from(httpRequest))

        StepVerifier.create(monoResult)
            .assertNext { assertThat(it).isEqualTo(HOST_ADDRESS) }
            .verifyComplete()
    }

    @Test
    fun `must return default key when neigher principal nor host address exist`() {
        val monoResult = keyResolver
            .resolve(
                MockServerWebExchange.from(
                    MockServerHttpRequest.post(URI_TEMPLATE)
                )
            )

        StepVerifier.create(monoResult)
            .assertNext { assertThat(it).isEqualTo(DEFAULT_KEY) }
            .verifyComplete()
    }

    @Test
    fun `must return fallback key on error`() {
        val monoResult = keyResolver
            .resolve(MockServerWebExchange.from(httpRequest))
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(null))

        StepVerifier.create(monoResult)
            .assertNext { assertThat(it).isEqualTo(FALLBACK_KEY) }
            .verifyComplete()
    }
}
