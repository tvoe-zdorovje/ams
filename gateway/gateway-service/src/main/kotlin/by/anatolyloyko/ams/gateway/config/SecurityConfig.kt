package by.anatolyloyko.ams.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http.authorizeExchange {
        it
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers(HttpMethod.POST, "/auth/**").permitAll()
            .anyExchange().authenticated()
    }
        .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .build()
}
