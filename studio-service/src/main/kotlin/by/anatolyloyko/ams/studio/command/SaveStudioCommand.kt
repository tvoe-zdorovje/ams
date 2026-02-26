package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.common.infrastructure.service.command.LoggedUserIdCommand
import by.anatolyloyko.ams.studio.model.Studio

/**
 * A command intended for creating a new studio or updating an existing one and returning the generated ID.
 *
 * @param input contains the studio data.
 * @param loggedUserId identifier of a logged user. This user is considered the owner of the studio is created.
 * @param brandId identifier of a brand which the studio belongs to.
 */
class SaveStudioCommand(
    input: Studio,
    loggedUserId: Long,
    val brandId: Long
) : LoggedUserIdCommand<Studio, Long>(input, loggedUserId)
