package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.RSAKey
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import kotlin.test.Test

private const val VAULT_SIZE = 2

class VaultTest : WithAssertions {
    @Test
    fun `rotate - must add key`() {
        val vault = Vault(VAULT_SIZE)

        assertThat(vault.getAll()).isEmpty()

        val key = mockk<RSAKey>()

        vault.rotate(key)

        assertThat(vault.getAll()).containsOnly(key)
    }

    @Test
    fun `rotate - must remove the oldest key if storage is full`() {
        val vault = Vault(VAULT_SIZE)

        for (i in 0 until VAULT_SIZE) {
            vault.rotate(mockk())
        }
        assertThat(vault.getAll()).hasSize(VAULT_SIZE)

        val key = mockk<RSAKey>()

        vault.rotate(key)

        assertThat(vault.getAll()).contains(key)
    }

    @Test
    fun `must return the newest key`() {
        val vault = Vault(VAULT_SIZE)

        for (i in 1 until VAULT_SIZE) {
            vault.rotate(mockk())
        }
        assertThat(vault.getAll()).hasSize(VAULT_SIZE-1)

        val key = mockk<RSAKey>()

        vault.rotate(key)

        assertThat(vault.get()).isSameAs(key)
    }

    @Test
    fun `must return all keys`() {
        val vault = Vault(VAULT_SIZE)

        val keys = List(VAULT_SIZE) { mockk<RSAKey>() }
        keys.forEach { vault.rotate(it) }

        val result = vault.getAll()

        assertThat(result).isEqualTo(keys.reversed())
    }
}
