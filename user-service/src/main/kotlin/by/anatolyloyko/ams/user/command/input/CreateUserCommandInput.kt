package by.anatolyloyko.ams.user.command.input

import by.anatolyloyko.ams.user.model.User

data class CreateUserCommandInput(
    val user: User,
    val externalId: String,
)
