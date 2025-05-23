package by.anatolyloyko.ams.auth.graphql.dto

data class LoginRequest(
    val userId: Long,
    val password: String
)
