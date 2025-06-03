package by.anatolyloyko.ams.auth.token.jwt.key.jwks.http

import by.anatolyloyko.ams.auth.token.jwt.key.KeyManager
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class JwksRestControllerTest : WithAssertions {
    private val keyManager = mockk<KeyManager> {
        every { getJwkSet() } returns mockk()
    }

    private val controller = JwksRestController(keyManager)

    @Test
    fun `must return JWKS`() {
        val expected = mapOf("mocked" to "map")
        every { keyManager.getJwkSet().toJSONObject(true) } returns expected

        val result = controller.getJwkSet()

        assertThat(result).isEqualTo(expected)
    }
}
