package by.anatolyloyko.ams.auth.token.model

data class TokenData(
    val userId: Long,
    val permissions: Map<Long, List<Permission>>
)
