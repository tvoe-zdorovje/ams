package by.anatolyloyko.ams.common.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.GraphQLError
import graphql.execution.DataFetcherResult
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.server.Encoding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.ErrorType
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: HttpSecurity, objectMapper: ObjectMapper): SecurityFilterChain = http
        .authorizeHttpRequests {
            it
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
        }
        .oauth2ResourceServer { resourceServerConfigurer ->
            resourceServerConfigurer.jwt(Customizer.withDefaults())
        }
        .exceptionHandling { exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.authenticationEntryPoint { request, response, exception ->
                response.status = HttpServletResponse.SC_OK
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.characterEncoding = Encoding.DEFAULT_CHARSET.name()
                val result = DataFetcherResult // whatever ¯\_(ツ)_/¯
                    .newResult<String>()
                    .error(
                        GraphQLError
                            .newError()
                            .errorType(ErrorType.UNAUTHORIZED)
                            .message(exception.localizedMessage)
                            .extensions(mapOf("classification" to ErrorType.UNAUTHORIZED))
                            .build()
                    )
                    .build()

                response.writer.write(objectMapper.writeValueAsString(result))
            }
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .csrf { it.disable() }
        .build()
}
