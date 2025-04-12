package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.brand.command.input.CreateBrandRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for create a new role for a brand.
 *
 * @param input contains brand ID, role data and list of role permissions.
 */
class CreateBrandRoleCommand(
    input: CreateBrandRoleInput
) : BaseCommand<CreateBrandRoleInput, Long>(input)
