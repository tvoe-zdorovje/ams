package by.anatolyloyko.ams.administration.brand.studio.graphql.dto

data class CreateStudioRoleRequest(
    val studioId: Long,
    val name: String,
    val description: String,
    val permissions: List<Long> = emptyList()
)
