package by.anatolyloyko.ams.gateway.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Component

@Component
class TokenExchangeWebFilterFactory(
    private val redisTemplate: ReactiveStringRedisTemplate,
    @param:Value("\${ams.redis.topic.user-tokens.key-prefix}")
    private val redisKeyPrefix: String
) {
    fun build() = TokenExchangeWebFilter(redisTemplate, redisKeyPrefix)
}
