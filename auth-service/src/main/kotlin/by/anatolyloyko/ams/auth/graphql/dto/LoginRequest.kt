package by.anatolyloyko.ams.auth.graphql.dto

data class LoginRequest(
    val phoneNumber: String,
    val password: CharArray,
)
