package by.anatolyloyko.ams.administration.role.command

import by.anatolyloyko.ams.administration.role.command.input.SaveRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for saving a new role or updating existing one and returning its ID.
 *
 * @param input the role data.
 */
class SaveRoleCommand(
    input: SaveRoleInput
) : BaseCommand<SaveRoleInput, Long>(input)
