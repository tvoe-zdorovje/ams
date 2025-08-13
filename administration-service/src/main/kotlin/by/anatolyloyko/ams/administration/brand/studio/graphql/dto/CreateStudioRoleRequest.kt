package by.anatolyloyko.ams.administration.brand.studio.graphql.dto

data class CreateStudioRoleRequest(
    val organizationId: Long,
    val name: String,
    val description: String,
    val permissions: List<Long> = emptyList()
)
