package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for creating a new brand  or updating an existing one and returning its ID.
 *
 * @param input contains the brand data.
 */
class SaveBrandCommand(
    input: Brand
) : BaseCommand<Brand, Long>(input)
