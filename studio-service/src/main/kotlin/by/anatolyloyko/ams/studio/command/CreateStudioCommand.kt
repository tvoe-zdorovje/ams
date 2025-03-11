package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand
import by.anatolyloyko.ams.studio.model.Studio

/**
 * A command intended for creating a new studio and returning the generated ID.
 *
 * @param input the studio data for creation.
 */
class CreateStudioCommand(
    input: Studio
) : BaseCommand<Studio, Long>(input)
