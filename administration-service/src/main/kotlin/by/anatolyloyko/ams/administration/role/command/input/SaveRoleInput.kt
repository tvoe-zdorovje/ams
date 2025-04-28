package by.anatolyloyko.ams.administration.role.command.input

import by.anatolyloyko.ams.administration.role.model.Role

data class SaveRoleInput(
    val role: Role,
    val permissions: List<Long> = emptyList(),
)
