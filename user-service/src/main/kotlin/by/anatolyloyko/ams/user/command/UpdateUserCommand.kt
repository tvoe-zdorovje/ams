package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand
import by.anatolyloyko.ams.user.command.input.UpdateUserCommandInput

/**
 * A command intended for updating a user and returning his ID.
 *
 * @param input contains updated user data.
 */
class UpdateUserCommand(
    input: UpdateUserCommandInput
) : BaseCommand<UpdateUserCommandInput, Long>(input)
