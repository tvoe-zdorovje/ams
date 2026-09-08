package by.anatolyloyko.ams.auth.token.action

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisPublishTokenAction(
    private val redis: StringRedisTemplate,
    @param:Value("\${jwt.time-of-life}")
    private val timeToLive: Long,
    @param:Value("\${ams.redis.topic.user-tokens.key-prefix}")
    private val keyPrefix: String
) : PublishTokenAction {
    override fun invoke(key: String, token: String) = redis.opsForValue().set(
        "$keyPrefix$key",
        token,
        timeToLive,
        TimeUnit.MILLISECONDS
    )
}
