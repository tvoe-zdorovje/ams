package by.anatolyloyko.ams.auth.token.command.input

import by.anatolyloyko.ams.auth.user.model.User

data class GenerateTokenCommandInput(
    val user: User,
)
