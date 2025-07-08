package by.anatolyloyko.ams.brand.graphql.dto

data class UpdateBrandRequest(
    val organizationId: Long,
    val name: String,
    val description: String,
)
