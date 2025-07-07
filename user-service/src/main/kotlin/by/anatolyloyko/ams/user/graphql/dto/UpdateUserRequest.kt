package by.anatolyloyko.ams.user.graphql.dto

data class UpdateUserRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)
