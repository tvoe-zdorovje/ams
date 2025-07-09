package by.anatolyloyko.ams.administration.user.query.input

data class GetUserRolesQueryInput(
    val userId: Long,
    val organizationId: Long
)
