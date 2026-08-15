package by.anatolyloyko.ams.auth.token.action

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.util.concurrent.TimeUnit

private const val TIME_TO_LIVE = 300000L

private const val KEY = "708e421b-d2c5-42b9-9114-dc2b9109bb49"

private const val TOKEN = "access-token"

class RedisPublishTokenActionTest {
    private val valueOperations = mockk<ValueOperations<String, String>>(relaxed = true)

    private val redis = mockk<StringRedisTemplate> {
        every { opsForValue() } returns valueOperations
    }

    private val action = RedisPublishTokenAction(redis, TIME_TO_LIVE)

    @Test
    fun `must publish token to redis with prefixed key and ttl`() {
        action(KEY, TOKEN)

        verify(exactly = 1) {
            valueOperations.set(
                "$KEY_PREFIX$KEY",
                TOKEN,
                TIME_TO_LIVE,
                TimeUnit.MILLISECONDS
            )
        }
    }
}
