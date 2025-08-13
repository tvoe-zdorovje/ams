package by.anatolyloyko.ams.administration.user.command.input

data class UserRolesInput(
    val userId: Long,
    val organizationId: Long,
    val roles: List<Long>
)
