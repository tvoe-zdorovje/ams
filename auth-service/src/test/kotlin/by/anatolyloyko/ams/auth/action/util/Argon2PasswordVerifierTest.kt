package by.anatolyloyko.ams.auth.action.util

import de.mkammerer.argon2.Argon2Factory
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class Argon2PasswordVerifierTest : WithAssertions {
    private val argo2PasswordVerifier: Argon2PasswordVerifier = Argon2PasswordVerifier()

    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    private val password = "tankist_1998".toCharArray()

    private val hash = argon2.hash(1, Short.MAX_VALUE+1, 2, password)

    @Test
    fun `must return true if password matches to hash`() {
        val result = argo2PasswordVerifier.verify(password, hash)

        assertThat(result).isTrue()
    }

    @Test
    fun `must return false if password does not match to hash`() {
        val result = argo2PasswordVerifier.verify("wrong passwsord".toCharArray(), hash)

        assertThat(result).isFalse()
    }

    @Test
    fun `must return false if password is empty`() {
        val result = argo2PasswordVerifier.verify(CharArray(0), hash)

        assertThat(result).isFalse()
    }

    @Test
    fun `must return false if hash is empty`() {
        val result = argo2PasswordVerifier.verify(password, "")

        assertThat(result).isFalse()
    }
}
