package by.anatolyloyko.ams.gateway.config

import by.anatolyloyko.ams.gateway.filter.TokenExchangeWebFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    @param:Value("\${ams.auth-server.auth-uri}")
    private val authUri: String,
    @param:Value("\${ams.auth-server.token-uri}")
    private val tokenUri: String,
) {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        tokenExchangeWebFilter: TokenExchangeWebFilter
    ): SecurityWebFilterChain = http.authorizeExchange {
        it
            .pathMatchers("/actuator/**").permitAll()
            .anyExchange().authenticated()
    }
        .oauth2ResourceServer {
            it.jwt(Customizer.withDefaults())
                .authenticationEntryPoint { exchange, exception ->
                    with(exchange.response) {
                        statusCode = HttpStatus.UNAUTHORIZED
                        headers.contentType = MediaType.APPLICATION_GRAPHQL_RESPONSE
                        val body = """
                            {
                                "error": "unauthorized",
                                "message": "${exception.message}",
                                "auth_uri": "$authUri",
                                "token_uri": "$tokenUri"
                            }
                            """.trimIndent()
                        writeWith(
                            Mono.just(bufferFactory().wrap(body.toByteArray()))
                        )
                    }
                }
        }
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .addFilterAfter(tokenExchangeWebFilter, SecurityWebFiltersOrder.AUTHORIZATION)
        .build()
}
