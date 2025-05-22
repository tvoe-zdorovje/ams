package by.anatolyloyko.ams.auth.jwt.graphql.dto

data class LoginRequest(
    val email: String,
    val phoneNumber: String,
    val password: String
)
