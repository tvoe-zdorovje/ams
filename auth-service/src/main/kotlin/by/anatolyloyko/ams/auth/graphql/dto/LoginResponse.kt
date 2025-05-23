package by.anatolyloyko.ams.auth.graphql.dto

data class LoginResponse(
    val userId: Long,
    val accessToken: String,
)
