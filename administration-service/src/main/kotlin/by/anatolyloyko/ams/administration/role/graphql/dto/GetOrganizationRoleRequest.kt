package by.anatolyloyko.ams.administration.role.graphql.dto

data class GetOrganizationRoleRequest(
    val organizationId: Long,
    val roleId: Long
)
