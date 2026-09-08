package by.anatolyloyko.ams.gateway.config

import by.anatolyloyko.ams.gateway.filter.TokenExchangeWebFilter
import by.anatolyloyko.ams.gateway.filter.TokenExchangeWebFilterFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.WebFilterChainProxy
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import reactor.test.StepVerifier

private const val AUTH_URI = "http://auth-service/oauth2/authorize"

private const val TOKEN_URI = "http://auth-service/oauth2/token"

class SecurityConfigTest : WithAssertions {
    private val config = SecurityConfig(AUTH_URI, TOKEN_URI)

    private val jwtDecoder: ReactiveJwtDecoder = mockk()

    private val tokenExchangeWebFilterFactory: TokenExchangeWebFilterFactory = mockk()

    @Test
    fun `actuator security chain must permit actuator requests`() {
        val exchange = MockServerWebExchange.from(
            MockServerHttpRequest.get("/actuator/health")
        )
        var chainCalled = false
        val chain = WebFilterChain {
            chainCalled = true
            Mono.empty()
        }

        val filters = config.actuatorSecurityWebFilterChain()
        filters
            .matches(exchange)
            .test()
            .expectNext(true)
            .verifyComplete()
        filters
            .matches(MockServerWebExchange.from(MockServerHttpRequest.get("/graphql")))
            .test()
            .expectNext(false)
            .verifyComplete()
        StepVerifier.create(
            WebFilterChainProxy(filters).filter(exchange, chain)
        )
            .verifyComplete()

        assertThat(chainCalled).isTrue()
    }

    @Test
    fun `security chain must return graphql unauthorized response`() {
        every { jwtDecoder.decode("bad-token") } returns Mono.error(BadJwtException("Invalid token"))
        every { tokenExchangeWebFilterFactory.build() } returns mockk<TokenExchangeWebFilter>(relaxed = true)

        WebTestClient
            .bindToWebHandler { Mono.empty() }
            .webFilter(WebFilterChainProxy(config.securityWebFilterChain(http(), tokenExchangeWebFilterFactory)))
            .build()
            .post()
            .uri("/graphql")
            .header(HttpHeaders.AUTHORIZATION, "Bearer bad-token")
            .exchange()
            .expectStatus().isUnauthorized
            .expectHeader().contentType(MediaType.APPLICATION_GRAPHQL_RESPONSE)
            .expectBody<String>()
            .value { body: String ->
                assertThat(body).contains(
                    "\"error\": \"unauthorized\"",
                    "\"message\": \"Invalid token\"",
                    "\"auth_uri\": \"$AUTH_URI\"",
                    "\"token_uri\": \"$TOKEN_URI\""
                )
            }
    }

    @Test
    fun `security chain must build token exchange web filter`() {
        every { tokenExchangeWebFilterFactory.build() } returns mockk<TokenExchangeWebFilter>(relaxed = true)

        config.securityWebFilterChain(http(), tokenExchangeWebFilterFactory)

        verify(exactly = 1) { tokenExchangeWebFilterFactory.build() }
    }

    private fun http(): ServerHttpSecurity = ServerHttpSecurity.http()
        .oauth2ResourceServer {
            it.jwt { jwt -> jwt.jwtDecoder(jwtDecoder) }
        }
}
