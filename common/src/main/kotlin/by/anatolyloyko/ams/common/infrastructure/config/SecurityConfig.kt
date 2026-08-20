package by.anatolyloyko.ams.common.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .authorizeHttpRequests {
            it
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
        }
        .oauth2ResourceServer { resourceServerConfigurer ->
            resourceServerConfigurer.jwt(Customizer.withDefaults())
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .csrf { it.disable() }
        .build()
}
