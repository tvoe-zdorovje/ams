package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for unassigning roles from a user.
 *
 * @param input contains user ID and list of role IDs.
 */
class UnassignRolesCommand(
    input: UserRolesInput
) : BaseCommand<UserRolesInput, Unit>(input)
