package by.anatolyloyko.ams.administration.role.graphql.dto

data class SaveRoleRequest(
    val id: Long?,
    val name: String,
    val description: String,
    val permissions: List<Long>,
)
