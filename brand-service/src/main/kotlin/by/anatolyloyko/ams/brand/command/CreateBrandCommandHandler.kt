package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateBrandCommand}.
 *
 * Creates a new brand based on the provided data.
 */
@Component
class CreateBrandCommandHandler(
    private val createBrandAction: CreateBrandAction
) : BaseCommandHandler<CreateBrandCommand, Long>() {
    override fun handleInternal(command: CreateBrandCommand): Long = createBrandAction(command.input)
}
