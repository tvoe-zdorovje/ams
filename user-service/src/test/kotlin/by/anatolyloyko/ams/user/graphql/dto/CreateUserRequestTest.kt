package by.anatolyloyko.ams.user.graphql.dto

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateUserRequestTest : WithAssertions {
    private val request = CreateUserRequest(
        password = "strong_password".toCharArray(),
        firstName = "first_name",
        lastName = "last_name",
        phoneNumber = "+375297671245",
    )

    @Test
    fun testEquals() {
        assertThat(request == request).isTrue()
        assertThat(request == request.copy()).isTrue()
        assertThat(request == request.copy(password = "weak_password".toCharArray())).isTrue()

        assertThat(request.equals(null)).isFalse()
        assertThat(request.equals(Object())).isFalse()
        assertThat(request == request.copy(firstName = "different_first_name")).isFalse()
        assertThat(request == request.copy(lastName = "different_last_name")).isFalse()
        assertThat(request == request.copy(phoneNumber = "+375297671246")).isFalse()
    }

    @Test
    fun testHashCode() {
        assertThat(request.hashCode()).isEqualTo(request.hashCode())
        assertThat(request.hashCode()).isEqualTo(request.copy().hashCode())
        assertThat(request.hashCode()).isEqualTo(request.copy(password = "weak_password".toCharArray()).hashCode())

        assertThat(request.hashCode()).isNotEqualTo(request.copy(firstName = "different_first_name").hashCode())
        assertThat(request.hashCode()).isNotEqualTo(request.copy(lastName = "different_last_name").hashCode())
        assertThat(request.hashCode()).isNotEqualTo(request.copy(phoneNumber = "+375297671246").hashCode())
    }

    @Test
    fun testToString() {
        assertThat(request.toString()).doesNotContain("password")
    }
}
