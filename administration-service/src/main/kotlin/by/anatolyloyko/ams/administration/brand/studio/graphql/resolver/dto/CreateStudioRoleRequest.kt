package by.anatolyloyko.ams.administration.brand.studio.graphql.resolver.dto

data class CreateStudioRoleRequest(
    val studioId: Long,
    val name: String,
    val description: String,
    val permissions: List<Long> = emptyList()
)
