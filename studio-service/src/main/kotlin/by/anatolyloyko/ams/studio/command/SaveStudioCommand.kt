package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand
import by.anatolyloyko.ams.studio.model.Studio

/**
 * A command intended for creating a new studio or updating an existing one and returning the generated ID.
 *
 * @param input contains the studio data.
 */
class SaveStudioCommand(
    input: Studio
) : BaseCommand<Studio, Long>(input)
