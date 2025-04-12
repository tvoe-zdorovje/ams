package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.brand.action.AssignStudiosAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link AssignStudiosCommand}.
 *
 * Assigns a provided list of studio IDs to a brand.
 */
@Component
class AssignStudiosCommandHandler(
    private val assignStudiosAction: AssignStudiosAction
) : BaseCommandHandler<AssignStudiosCommand, Unit>() {
    override fun handleInternal(command: AssignStudiosCommand) = assignStudiosAction(
        brandId = command.input.brandId,
        studios = command.input.studios
    )
}
