package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.action.SaveBrandAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateBrandCommand}.
 *
 * Creates a new brand or updates an existing one based on the provided data.
 */
@Component
class SaveBrandCommandHandler(
    private val saveBrandAction: SaveBrandAction
) : BaseCommandHandler<SaveBrandCommand, Long>() {
    override fun handleInternal(command: SaveBrandCommand): Long = saveBrandAction(command.input)
}
