package by.anatolyloyko.ams.studio.graphql.dto

data class UpdateStudioRequest(
    val organizationId: Long,
    val name: String,
    val description: String,
)
