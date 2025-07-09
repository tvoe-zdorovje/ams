package by.anatolyloyko.ams.administration.user.graphql.dto

data class GetUserRolesRequest(
    val userId: Long,
    val organizationId: Long
)
