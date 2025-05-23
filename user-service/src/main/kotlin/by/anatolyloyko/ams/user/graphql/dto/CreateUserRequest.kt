package by.anatolyloyko.ams.user.graphql.dto

data class CreateUserRequest(
    val password: CharArray,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateUserRequest

        if (!password.contentEquals(other.password)) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = password.contentHashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }

    override fun toString(): String {
        return "CreateUserRequest(firstName='$firstName', lastName='$lastName', phoneNumber='$phoneNumber')"
    }
}
