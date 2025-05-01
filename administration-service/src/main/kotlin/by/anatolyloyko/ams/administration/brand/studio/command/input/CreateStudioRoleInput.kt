package by.anatolyloyko.ams.administration.brand.studio.command.input

import by.anatolyloyko.ams.administration.role.model.Role

data class CreateStudioRoleInput(
    val studioId: Long,
    val role: Role,
    val permissions: List<Long>,
)
