package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.KeyType
import com.nimbusds.jose.jwk.RSAKey
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

private const val KID_FORMAT = "kid-%s"

class KeyGeneratorTest : WithAssertions {
    private val generator = KeyGenerator(KID_FORMAT)

    @Test
    fun `must generate RSA Key`() {
        val result = generator.generate()

        assertThat(result).isOfAnyClassIn(RSAKey::class.java)
        assertThat(result.keyID).matches(KID_FORMAT.format("\\d{13}"))
        assertThat(result.keyType).isEqualTo(KeyType.RSA)
    }
}
