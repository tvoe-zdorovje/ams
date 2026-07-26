package by.anatolyloyko.ams.gateway.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.ReactiveSecurityContextHolder

const val DEFAULT_KEY = "anonymous"
const val FALLBACK_KEY = "fallback_anonymous"

@Configuration
class RequestRateLimiterConfig {
    @Bean
    fun keyResolver(): KeyResolver = KeyResolver { exchange ->
        ReactiveSecurityContextHolder
            .getContext()
            .mapNotNull { it.authentication.name }
            .defaultIfEmpty(
                exchange
                    .request
                    .remoteAddress
                    ?.address
                    ?.hostAddress
                    ?: DEFAULT_KEY
            )
            .onErrorReturn(FALLBACK_KEY)
    }
}
