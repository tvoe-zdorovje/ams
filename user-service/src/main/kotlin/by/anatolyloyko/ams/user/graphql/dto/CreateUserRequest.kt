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

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }

    override fun toString(): String =
        "CreateUserRequest(firstName='$firstName', lastName='$lastName', phoneNumber='$phoneNumber')"
}
