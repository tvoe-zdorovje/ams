package by.anatolyloyko.ams.auth.token.action

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

internal const val KEY_PREFIX = "auth:token:userID:"

@Component
class RedisPublishTokenAction(
    private val redis: StringRedisTemplate,
    @param:Value("\${jwt.time-of-life}")
    private val timeToLive: Long,
) : PublishTokenAction {
    override fun invoke(key: String, token: String) = redis.opsForValue().set(
        "$KEY_PREFIX$key",
        token,
        timeToLive,
        TimeUnit.MILLISECONDS
    )
}
