package by.anatolyloyko.ams.administration.user.graphql.dto

data class AssignRolesRequest(
    val userId: Long,
    val organizationId: Long,
    val roles: List<Long>
)
