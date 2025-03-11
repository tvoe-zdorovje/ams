package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand
import by.anatolyloyko.ams.user.model.User

/**
 * A command intended for creating a new user and returning the generated ID.
 *
 * @param input the user data for creation.
 */
class CreateUserCommand(
    input: User
) : BaseCommand<User, Long>(input)
