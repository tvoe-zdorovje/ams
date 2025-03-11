package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for creating a new brand and returning the generated ID.
 *
 * @param input the brand data for creation.
 */
class CreateBrandCommand(
    input: Brand
) : BaseCommand<Brand, Long>(input)
