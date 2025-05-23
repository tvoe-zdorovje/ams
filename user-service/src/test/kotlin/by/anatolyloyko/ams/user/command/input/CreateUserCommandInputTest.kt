package by.anatolyloyko.ams.user.command.input

import by.anatolyloyko.ams.user.NEW_USER
import by.anatolyloyko.ams.user.USER
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateUserCommandInputTest : WithAssertions {
    private val input = CreateUserCommandInput(
        password = "strong_password".toCharArray(),
        user = USER
    )

    @Test
    fun testEquals() {
        assertThat(input == input).isTrue()
        assertThat(input == input.copy()).isTrue()
        assertThat(input == input.copy(password = "weak_password".toCharArray())).isTrue()

        assertThat(input.equals(null)).isFalse()
        assertThat(input.equals(Object())).isFalse()
        assertThat(input == input.copy(user = NEW_USER)).isFalse()
    }

    @Test
    fun testHashCode() {
        assertThat(input.hashCode()).isEqualTo(input.hashCode())
        assertThat(input.hashCode()).isEqualTo(input.copy().hashCode())
        assertThat(input.hashCode()).isEqualTo(input.copy(password = "weak_password".toCharArray()).hashCode())

        assertThat(input.hashCode()).isNotEqualTo(input.copy(user = NEW_USER).hashCode())
    }

    @Test
    fun testToString() {
        assertThat(input.toString()).doesNotContain("password")
    }
}
