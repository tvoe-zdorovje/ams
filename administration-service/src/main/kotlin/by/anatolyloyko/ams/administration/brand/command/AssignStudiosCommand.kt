package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.brand.command.input.AssignStudiosInput
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for assigning studios to a brand.
 *
 * @param input contains brand ID and list of studio IDs.
 */
class AssignStudiosCommand(
    input: AssignStudiosInput
) : BaseCommand<AssignStudiosInput, Unit>(input)
