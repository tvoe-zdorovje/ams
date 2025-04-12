package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for assigning roles to a user.
 *
 * @param input contains user ID and list of role IDs.
 */
class AssignRolesCommand(
    input: UserRolesInput
) : BaseCommand<UserRolesInput, Unit>(input)
