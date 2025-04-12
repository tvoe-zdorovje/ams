package by.anatolyloyko.ams.administration.brand.studio.command

import by.anatolyloyko.ams.administration.brand.studio.command.input.CreateStudioRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for create a new role for a studio.
 *
 * @param input contains studio ID, role data and list of role permissions.
 */
class CreateStudioRoleCommand(
    input: CreateStudioRoleInput
) : BaseCommand<CreateStudioRoleInput, Long>(input)
