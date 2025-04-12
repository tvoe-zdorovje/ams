package by.anatolyloyko.ams.administration.brand.graphql.dto

data class CreateBrandRoleRequest(
    val brandId: Long,
    val name: String,
    val description: String,
    val permissions: List<Long> = emptyList()
)
