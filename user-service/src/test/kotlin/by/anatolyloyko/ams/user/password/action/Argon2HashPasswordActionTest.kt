package by.anatolyloyko.ams.user.password.action

import by.anatolyloyko.ams.user.USER_PASSWORD
import de.mkammerer.argon2.Argon2Factory
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class Argon2HashPasswordActionTest : WithAssertions {
    private val action = Argon2HashPasswordAction()

    @Test
    fun `must hash and return password`() {
        val rawPassword = USER_PASSWORD.toCharArray()

        val hashedPassword = action(rawPassword)

        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
        assertThat(hashedPassword).isNotEqualTo(rawPassword)
        assertThat(argon2.verify(hashedPassword, rawPassword)).isTrue()
        assertThat(argon2.verify(hashedPassword, "wrong_password".toCharArray())).isFalse()
    }
}
