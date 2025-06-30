package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.WithAssertions
import org.slf4j.Logger
import java.lang.reflect.Field
import kotlin.math.log
import kotlin.test.Test

private const val KID_FORMAT = "kid-%s"

private const val VAULT_SIZE = 2

class KeyManagerTest : WithAssertions {
    private val keyManager: KeyManager = KeyManager(KID_FORMAT, VAULT_SIZE)

    private val keyGenerator = mockk<KeyGenerator>()

    private val vault = mockk<Vault>()

    private val logger = mockk<Logger>(relaxed = true)

    init {
        setPrivateField(keyManager, "generator", keyGenerator)
        setPrivateField(keyManager, "vault", vault)
        setPrivateField(keyManager, "log", logger)
    }

    @Test
    fun `must create vault with correct size`() {
        val vault = getPrivateField<Vault>(KeyManager(KID_FORMAT, VAULT_SIZE), "vault")
        val size = getPrivateField<Int>(vault, "size")

        assertThat(size).isEqualTo(VAULT_SIZE)
    }

    @Test
    fun `must return an actual key`() {
        val expectedKey = mockk<RSAKey>()
        every { vault.get() } returns expectedKey

        val actualKey = keyManager.getKey()

        assertThat(actualKey).isSameAs(expectedKey)
        verify(exactly = 1) { vault.get() }
    }

    @Test
    fun `must return JWK Set`() {
        val expectedKey = mockk<RSAKey> {
            every { toPublicJWK() } returns this
        }
        val expectedJwks = JWKSet(expectedKey)
        every { vault.getAll() } returns listOf(expectedKey)

        val actualJwks = keyManager.getJwkSet()

        assertThat(actualJwks).isEqualTo(expectedJwks)
        verify(exactly = 1) {
            vault.getAll()
            expectedKey.toPublicJWK()
        }
    }

    @Test
    fun `must rotate keys`() {
        val key = mockk<RSAKey>()
        every { keyGenerator.generate() } returns key
        every { vault.rotate(key) } returns Unit

        keyManager.rotateKeys()

        verifyOrder {
            logger.info("Scheduled key rotation")
            keyGenerator.generate()
            vault.rotate(key)
        }
    }

    companion object ReflectionUtils {
        private fun setPrivateField(instance: Any, fieldName: String, value: Any) {
            val field: Field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(instance, value)
        }

        private inline fun <reified T> getPrivateField(instance: Any, fieldName: String): T {
            val field: Field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true

            return field.get(instance) as T
        }
    }
}
