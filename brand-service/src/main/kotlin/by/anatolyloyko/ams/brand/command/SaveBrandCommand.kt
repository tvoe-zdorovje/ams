package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.common.infrastructure.service.command.LoggedUserIdCommand

/**
 * A command intended for creating a new brand  or updating an existing one and returning its ID.
 *
 * @param input contains the brand data.
 * @param loggedUserId identifier of a logged user. This user is considered the owner of the brand is created.
 */
class SaveBrandCommand(
    input: Brand,
    loggedUserId: Long,
) : LoggedUserIdCommand<Brand, Long>(input, loggedUserId)
