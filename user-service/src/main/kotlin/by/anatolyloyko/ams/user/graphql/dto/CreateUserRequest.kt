package by.anatolyloyko.ams.user.graphql.dto

data class CreateUserRequest(
    val externalId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)
